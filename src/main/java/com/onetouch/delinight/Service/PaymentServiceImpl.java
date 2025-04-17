/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.PayType;
import com.onetouch.delinight.DTO.CheckInDTO;
import com.onetouch.delinight.DTO.OrdersDTO;
import com.onetouch.delinight.DTO.PaymentDTO;
import com.onetouch.delinight.DTO.StoreDTO;
import com.onetouch.delinight.Entity.*;
import com.onetouch.delinight.Repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<OrdersDTO> readOrders(Long paymentId) {

        PaymentEntity paymentEntity = paymentRepository.findById(paymentId).get();
        List<OrdersEntity> ordersEntityList = paymentEntity.getOrdersEntityList();
        List<OrdersDTO> ordersDTOList = ordersEntityList.stream()
                .map(data->modelMapper.map(data,OrdersDTO.class)
                        .setStoreDTO(modelMapper.map(data.getStoreEntity(), StoreDTO.class))
                        .setCheckInDTO(modelMapper.map(data.getCheckInEntity(), CheckInDTO.class))).toList();

        return ordersDTOList;
    }

    @Override
    public List<PaymentDTO> findAllDate(Long totalId, PayType type) {

        log.info("totalId : {}, payType : {}", totalId, type);

        List<PaymentEntity> paymentEntityList;

        // payType 값에 따라 해당 Repository 메서드 호출
        if (type == PayType.CENTER) {
            log.info("센터 조회 시작: totalId {}", totalId);
            paymentEntityList = paymentRepository.findCenterForDate(totalId);
        }else if (type == PayType.BRANCH) {
            log.info("지점 조회 시작: totalId {}", totalId);
            paymentEntityList = paymentRepository.findBranchForDate(totalId);
        }else if (type == PayType.HOTEL) {
            log.info("호텔 조회 시작: totalId {}", totalId);
            paymentEntityList = paymentRepository.findHotelForDate(totalId);
        }else if (type == PayType.STORE) {
            log.info("가맹점 조회 시작: totalId {}", totalId);
            paymentEntityList = paymentRepository.findStoreForDate(totalId);
        }else {
            log.info("유효하지 않은 정산 타입입니다. type: {}", type);
            throw new IllegalArgumentException("유효하지 않은 정산 타입입니다.");
        }
        log.info("조회된 paymentEntityList: {}", paymentEntityList);

        // Entity → DTO
        List<PaymentDTO> paymentDTOList = paymentEntityList.stream()
                .map(data -> {
                    OrdersEntity order = data.getOrdersEntityList().getFirst();
                    log.info("변환된 order : {}", order);
                    StoreEntity store = order.getStoreEntity();
                    log.info("변환된 store : {}", store);
                    HotelEntity hotel = store.getHotelEntity();
                    log.info("변환된 hotel : {}", hotel);
                    BranchEntity branch = hotel.getBranchEntity();
                    log.info("변환된 branch : {}", branch);
                    CenterEntity center = branch.getCenterEntity();
                    log.info("변환된 center : {}", center);

                    String roomNumber = order.getCheckInEntity() != null ? String.valueOf(order.getCheckInEntity().getRoomEntity()) : null;
                    log.info("roomNumber : {}", roomNumber);

                    PaymentDTO paymentDTO = new PaymentDTO(
                            data.getId(),
                            data.getTotalAmount(),
                            data.getRegTime(),
                            order.getOrdersStatus().name(),
                            store.getName(),
                            hotel.getName(),
                            branch.getName(),
                            center.getName(),
                            roomNumber,
                            data.getPaidCheck()
                    );

                    log.info("완성된 PaymentDTO: {}", paymentDTO);
                    return paymentDTO;

                })
                .collect(Collectors.toList());

        log.info("최종 반환되는 paymentDTOList: {}", paymentDTOList);
        return paymentDTOList;
    }


}
