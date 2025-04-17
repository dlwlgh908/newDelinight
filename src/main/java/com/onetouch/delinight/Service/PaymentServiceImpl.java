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
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;


//    @Override
//    public List<OrdersDTO> readOrders(Long paymentId) {
//
//        PaymentEntity paymentEntity = paymentRepository.findById(paymentId).get();
//        List<OrdersEntity> ordersEntityList = paymentEntity.getOrdersEntityList();
//        List<OrdersDTO> ordersDTOList = ordersEntityList.stream()
//                .map(data->modelMapper.map(data,OrdersDTO.class)
//                        .setStoreDTO(modelMapper.map(data.getStoreEntity(), StoreDTO.class))
//                        .setCheckInDTO(modelMapper.map(data.getCheckInEntity(), CheckInDTO.class))).toList();
//
//        return ordersDTOList;
//    }

       

    @Override
    public List<PaymentDTO> findAllDate(Long totalId, PayType type) {

        log.info("totalId : {}, payType : {}", totalId, type);

        List<PaymentEntity> paymentEntityList;
        List<PaymentEntity> paymentEntityList2 =new ArrayList<>();
        List<OrdersEntity> ordersEntityList=new ArrayList<>();

        // payType 값에 따라 해당 Repository 메서드 호출
        if (type.equals(PayType.CENTER)) {
            log.info("센터 조회 시작: totalId {}", totalId);
            paymentEntityList = paymentRepository.findCenterForDate(totalId); // Center 결제 데이터 조회

        }else if (type.equals(PayType.BRANCH)) {
            log.info("지점 조회 시작: totalId {}", totalId);
            paymentEntityList = paymentRepository.findBranchForDate(totalId); // Branch 결제 데이터 조회
        }else if (type.equals(PayType.HOTEL)) {
            log.info("호텔 조회 시작: totalId {}", totalId);
            paymentEntityList = paymentRepository.findHotelForDate(totalId); // Hotel 결제 데이터 조회
        }else if (type.equals(PayType.STORE)) {
            log.info("가맹점 조회 시작: totalId {}", totalId);
            paymentEntityList = paymentRepository.findStoreForDate(totalId); // Store 결제 데이터 조회

            paymentEntityList.forEach(paymentEntity -> {
                paymentEntity.getOrdersEntityList().forEach(ordersEntity -> {
                    if (ordersEntity.getStoreEntity().getId().equals(totalId)) {
                        paymentEntityList2.add(PaymentEntity.builder()
                                .id(paymentEntity.getId()).orderType(paymentEntity.getOrderType()).
                                paidCheck(paymentEntity.getPaidCheck()).ordersEntityList(List.of(ordersEntity))
                                .build());
                    }
                });
            });
            log.info(ordersEntityList.size());


        }else {
            log.info("유효하지 않은 정산 타입입니다. type: {}", type);
            throw new IllegalArgumentException("유효하지 않은 정산 타입입니다.");
        }

        // Entity → DTO
        List<PaymentDTO> paymentDTOList = paymentEntityList2.stream()
                .map(data -> {
                    OrdersEntity order = data.getOrdersEntityList().getFirst(); // 결제 데이터에서 첫 번째 주문 Entity 가져옴
                    StoreEntity store = order.getStoreEntity();                 // 주문에서 매장 정보 가져옴
                    HotelEntity hotel = store.getHotelEntity();                 // 매장에서 호텔 정보 가져옴
                    BranchEntity branch = hotel.getBranchEntity();              // 호텔에서 지점 정보 가져옴
                    CenterEntity center = branch.getCenterEntity();             // 지점에서 센터 정보 가져옴

                    String roomNumber = order.getCheckInEntity() != null ? String.valueOf(order.getCheckInEntity().getRoomEntity()) : null; // 체크인 정보가 있으면 방 번호 가져옴

                    PaymentDTO paymentDTO = new PaymentDTO(
                            data.getId(),                       // 결제 ID
                            data.getTotalAmount(),              // 총 금액
                            data.getRegTime(),                  // 등록 시간
                            order.getOrdersStatus().name(),     // 주문 상태
                            store.getName(),                    // 매장 이름
                            hotel.getName(),                    // 호텔 이름
                            branch.getName(),                   // 지점 이름
                            center.getName(),                   // 센터 이름
                            roomNumber,                         // 방 번호
                            data.getPaidCheck()                 // 결제 상태(정산여부)
                    );

                    return paymentDTO; // DTO 반환

                })
                .collect(Collectors.toList()); // List 반환

        return paymentDTOList; // 최종 DTOList 반환
    }



}
