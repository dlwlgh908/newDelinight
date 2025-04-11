/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.OrderType;
import com.onetouch.delinight.Constant.OrdersStatus;
import com.onetouch.delinight.Constant.PaidCheck;
import com.onetouch.delinight.DTO.*;
import com.onetouch.delinight.Entity.OrdersEntity;
import com.onetouch.delinight.Entity.PaymentEntity;
import com.onetouch.delinight.Entity.StoreEntity;
import com.onetouch.delinight.Repository.OrdersRepository;
import com.onetouch.delinight.Repository.PaymentRepository;
import com.onetouch.delinight.Repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService{

    private final StoreRepository storeRepository;
    private final OrdersRepository ordersRepository;
    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;
    private final SseService sseService;

    @Override
    public Page<OrdersDTO> processList(Pageable pageable, String email) {
        Page<OrdersEntity> processEntityList = ordersRepository.findByStoreEntity_MembersEntity_EmailAndOrdersStatusNotAndOrdersStatusIsNot(email, OrdersStatus.DELIVERED, OrdersStatus.PENDING, pageable);
        Page<OrdersDTO> processDTOList =  processEntityList.map(data->modelMapper.map(data, OrdersDTO.class)
                .setCheckInDTO(modelMapper.map(data.getCheckInEntity(), CheckInDTO.class).setRoomDTO(modelMapper.map(data.getCheckInEntity().getRoomEntity(),RoomDTO.class).setHotelDTO(modelMapper.map(data.getCheckInEntity().getRoomEntity().getHotelEntity(),HotelDTO.class))))
                .setStoreDTO(modelMapper.map(data.getStoreEntity(), StoreDTO.class))
                .setOrdersItemDTOList(data.getOrdersItemEntities().stream().map(ordersItemEntity->modelMapper.map(ordersItemEntity, OrdersItemDTO.class).setMenuDTO(modelMapper.map(ordersItemEntity.getMenuEntity(),MenuDTO.class))).toList()));

        return processDTOList;
    }

    @Override
    public Page<OrdersDTO> completeList(Pageable pageable, String email) {
        Page<OrdersEntity> completeList = ordersRepository.findByStoreEntity_MembersEntity_EmailAndOrdersStatusIs(email, OrdersStatus.DELIVERED,  pageable);
        Page<OrdersDTO> completeDTOList =  completeList.map(data->modelMapper.map(data, OrdersDTO.class)
                .setCheckInDTO(modelMapper.map(data.getCheckInEntity(), CheckInDTO.class).setRoomDTO(modelMapper.map(data.getCheckInEntity().getRoomEntity(),RoomDTO.class).setHotelDTO(modelMapper.map(data.getCheckInEntity().getRoomEntity().getHotelEntity(),HotelDTO.class))))
                .setStoreDTO(modelMapper.map(data.getStoreEntity(), StoreDTO.class))
                .setOrdersItemDTOList(data.getOrdersItemEntities().stream().map(ordersItemEntity->modelMapper.map(ordersItemEntity, OrdersItemDTO.class).setMenuDTO(modelMapper.map(ordersItemEntity.getMenuEntity(),MenuDTO.class))).toList()));

        return completeDTOList;
    }

    @Override
    public StoreDTO findStoreByADMINEmail(String email) {
        StoreEntity store = storeRepository.findByMembersEntity_Email(email);
        StoreDTO dto = modelMapper.map(store, StoreDTO.class)
                .setHotelDTO(modelMapper.map(store.getHotelEntity(), HotelDTO.class))
                .setMemberDTO(modelMapper.map(store.getMembersEntity(), MembersDTO.class));
        return dto;
    }

    @Override
    public List<OrdersDTO> read(Long paymentNum) {

        PaymentEntity paymentEntity = paymentRepository.findById(paymentNum).get();
        List<OrdersEntity> ordersEntityList = paymentEntity.getOrdersEntityList();
        List<OrdersDTO> ordersDTOList = ordersEntityList.stream().map(data-> modelMapper.map(data, OrdersDTO.class)
                .setOrdersItemDTOList(data.getOrdersItemEntities().stream().map(data2->modelMapper.map(data2, OrdersItemDTO.class)
                        .setOrdersDTO(modelMapper.map(data2.getOrdersEntity(), OrdersDTO.class)).setMenuDTO(modelMapper.map(data2.getMenuEntity(),MenuDTO.class)
                                )).toList())
                .setStoreDTO(modelMapper.map(data.getStoreEntity(),StoreDTO.class))
                .setCheckInDTO(modelMapper.map(data.getCheckInEntity(), CheckInDTO.class))).toList();

        return ordersDTOList;
    }

    @Override
    public void changePayNow(Long ordersId, String memo) {
        OrdersEntity ordersEntity = ordersRepository.findById(ordersId).get();
        ordersEntity.setMemo(memo);
        ordersEntity.setOrdersStatus(OrdersStatus.AWAITING);
        ordersEntity.setAwaitingTime(LocalDateTime.now());
        PaymentEntity paymentEntity = paymentRepository.findByOrdersEntityList_Id(ordersId);
        paymentEntity.setOrderType(OrderType.PAYNOW);
        paymentEntity.setPaidCheck(PaidCheck.paid);
        ordersRepository.save(ordersEntity);
        paymentRepository.save(paymentEntity);
    }

    @Override
    public void changePayLater(Long ordersId, String memo) {
        OrdersEntity ordersEntity = ordersRepository.findById(ordersId).get();
        ordersEntity.setMemo(memo);
        ordersEntity.setOrdersStatus(OrdersStatus.AWAITING);
        ordersEntity.setAwaitingTime(LocalDateTime.now());
        PaymentEntity paymentEntity = paymentRepository.findByOrdersEntityList_Id(ordersId);
        paymentEntity.setOrderType(OrderType.PAYLATER);
        ordersRepository.save(ordersEntity);
        paymentRepository.save(paymentEntity);
        log.info("이치문 처럼안되는건가...?"+ordersEntity.getStoreEntity().getId());

        sseService.sendToSAdmin("S"+ordersEntity.getStoreEntity().getId(),"new-order",ordersId+"번 주문이 들어왔습니다.");

    }

    @Override
    public void changeStatus(Long ordersId, String ordersStatus) {
        OrdersEntity orders = ordersRepository.findById(ordersId).get();
        orders.setOrdersStatus(checkStatus(ordersStatus));
        if(ordersStatus.equals("preparing")){
            orders.setPreparingTime(LocalDateTime.now());
        }
        else if(ordersStatus.equals("delivering")){
            orders.setDeliveringTime(LocalDateTime.now());
        }
        else {
            orders.setDeliveredTime(LocalDateTime.now());
        }
        ordersRepository.save(orders);
    }

    @Override
    public OrdersStatus checkStatus(String ordersStatus) {
        if(ordersStatus.equals("preparing")){
            return OrdersStatus.PREPARING;
        }
        else if(ordersStatus.equals("delivering")){
            return OrdersStatus.DELIVERING;
        }
        else {
            return OrdersStatus.DELIVERED;
        }
    }

    @Override
    public boolean pendingCheck(Long paymentId) {
        PaymentEntity paymentEntity = paymentRepository.findById(paymentId).get();
        boolean result = paymentEntity.getOrdersEntityList().getFirst().getOrdersStatus().equals(OrdersStatus.PENDING);
        return result;
    }

}
