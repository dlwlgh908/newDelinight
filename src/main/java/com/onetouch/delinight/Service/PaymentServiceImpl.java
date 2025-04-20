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
        List<PaymentEntity> paymentList = new ArrayList<>();
        List<PaymentEntity> filteredPaymentList = new ArrayList<>();

        if (type == PayType.STORE) {
            paymentList = paymentRepository.findStoreForDate(totalId);
            paymentList.forEach(payment -> {
                payment.getOrdersEntityList().forEach(order -> {
                    if (order.getStoreEntity().getId().equals(totalId)) {
                        filteredPaymentList.add(
                                PaymentEntity.builder()
                                        .id(payment.getId())
                                        .orderType(payment.getOrderType())
                                        .paidCheck(payment.getPaidCheck())
                                        .totalAmount(payment.getTotalAmount())
                                        .paymentTime(payment.getRegTime())
                                        .ordersEntityList(List.of(order))
                                        .build()
                        );
                    }
                });
            });
        } else if (type == PayType.HOTEL) {
            paymentList = paymentRepository.findHotelForDate(totalId);
            paymentList.forEach(payment -> {
                payment.getOrdersEntityList().forEach(order -> {
                    if (order.getStoreEntity().getHotelEntity().getId().equals(totalId)) {
                        filteredPaymentList.add(
                                PaymentEntity.builder()
                                        .id(payment.getId())
                                        .orderType(payment.getOrderType())
                                        .paidCheck(payment.getPaidCheck())
                                        .totalAmount(payment.getTotalAmount())
                                        .paymentTime(payment.getRegTime())
                                        .ordersEntityList(List.of(order))
                                        .build()
                        );
                    }
                });
            });
        } else if (type == PayType.BRANCH) {
            paymentList = paymentRepository.findBranchForDate(totalId);
            paymentList.forEach(payment -> {
                payment.getOrdersEntityList().forEach(order -> {
                    if (order.getStoreEntity().getHotelEntity().getBranchEntity().getId().equals(totalId)) {
                        filteredPaymentList.add(
                                PaymentEntity.builder()
                                        .id(payment.getId())
                                        .orderType(payment.getOrderType())
                                        .paidCheck(payment.getPaidCheck())
                                        .totalAmount(payment.getTotalAmount())
                                        .paymentTime(payment.getRegTime())
                                        .ordersEntityList(List.of(order))
                                        .build()
                        );
                    }
                });
            });
        } else if (type == PayType.CENTER) {
            paymentList = paymentRepository.findCenterForDate(totalId);
            paymentList.forEach(payment -> {
                payment.getOrdersEntityList().forEach(order -> {
                    if (order.getStoreEntity().getHotelEntity().getBranchEntity().getCenterEntity().getId().equals(totalId)) {
                        filteredPaymentList.add(
                                PaymentEntity.builder()
                                        .id(payment.getId())
                                        .orderType(payment.getOrderType())
                                        .paidCheck(payment.getPaidCheck())
                                        .totalAmount(payment.getTotalAmount())
                                        .paymentTime(payment.getRegTime())
                                        .ordersEntityList(List.of(order))
                                        .build()
                        );
                    }
                });
            });
        }

        // DTO 변환
        return filteredPaymentList.stream()
                .map(payment -> {
                    OrdersEntity order = payment.getOrdersEntityList().get(0);
                    StoreEntity store = order.getStoreEntity();
                    HotelEntity hotel = store.getHotelEntity();
                    BranchEntity branch = hotel.getBranchEntity();
                    CenterEntity center = branch.getCenterEntity();

                    String roomNumber = order.getCheckInEntity() != null
                            ? String.valueOf(order.getCheckInEntity().getRoomEntity())
                            : null;

                    return new PaymentDTO(
                            payment.getId(),
                            payment.getTotalAmount(),
                            payment.getRegTime(),
                            order.getOrdersStatus().name(),
                            store.getName(),
                            hotel.getName(),
                            branch.getName(),
                            center.getName(),
                            roomNumber,
                            payment.getPaidCheck()
                    );
                })
                .collect(Collectors.toList());
    }



    /*@Override
    public List<PaymentDTO> findAllDate(Long totalId, PayType type) {

        log.info("totalId : {}, payType : {}", totalId, type);

        // 정산 타입에 따라 해당하는 결제 Entity 리스트 가져오기
        List<PaymentEntity> paymentEntityList = switch (type){
            case CENTER -> paymentRepository.findCenterForDate(totalId);            // CENTER 기준 조회
            case BRANCH -> paymentRepository.findBranchForDate(totalId);            // BRANCH 기준 조회
            case HOTEL -> paymentRepository.findHotelForDate(totalId);              // HOTEL 기준 조회
            case STORE -> paymentRepository.findStoreForDate(totalId);              // STORE 기준 조회
            default -> throw new IllegalArgumentException("유효하지 않는 정산 타입");
        };

        log.info("조회된 결제 건수 : {}", paymentEntityList.size());

        // 최종 결과를 담을 새 결제 리스트
        List<PaymentEntity> filterPayment = new ArrayList<>();

        // 가져온 결제 데이터(paymentEntityList) 하나씩 순회
        for (PaymentEntity paymentEntity : paymentEntityList) {
            // 각 결제에 연결된 주문 리스트 순회
            for (OrdersEntity order : paymentEntity.getOrdersEntityList()) {

                // 주문에 연결된 StoreId 확인
                Long storeId = order.getStoreEntity() != null ? order.getStoreEntity().getId() : null;
                log.info("storeId: {}, totalId: {}", storeId, totalId);

                // 정산 타입에 따라 주문이 속한 ID가 totalId와 일치하는지 비교
                boolean isMatch = switch (type){
                    case CENTER -> order.getStoreEntity().getHotelEntity().getBranchEntity().getCenterEntity().getId().equals(totalId); // CENTER ID 비교
                    case BRANCH -> order.getStoreEntity().getHotelEntity().getBranchEntity().getId().equals(totalId);                   // BRANCH ID 비교
                    case HOTEL -> order.getStoreEntity().getHotelEntity().getId().equals(totalId);                                      // HOTEL ID 비교
                    case STORE  -> storeId != null && storeId.equals(totalId);                                                          // STORE ID 비교
                    default -> false;
                };

                log.info("일치 여부: {}", isMatch ? "일치" : "불일치");

                // 일치하는 주문만 추출해 새로운 paymentEntity 생성 및 리스트 추가
                if (isMatch) {
                    filterPayment.add(PaymentEntity.builder()
                            .id(paymentEntity.getId())                      // 기존 결제 ID 복사
                            .orderType(paymentEntity.getOrderType())        // 주문 유형 복사
                            .paidCheck(paymentEntity.getPaidCheck())        // 정산 여부 복사
                            .totalAmount(paymentEntity.getTotalAmount())    // 총 금액 복사.regTime(paymentEntity.getRegTime())            // 등록 시간 복사
                            .ordersEntityList(List.of(order))               // 주문 리스트를 현재 주문 하나만 포함하여 설정
                            .build());                                      // 새 PaymentEntity 객체 생성
                }
            }
        }

        log.info("필터링 된 결제 건수 : {}", filterPayment.size());

        // Step 3. DTO 변환
        return filterPayment.stream()
                .map(payment -> {
                    OrdersEntity order = payment.getOrdersEntityList().get(0); // 첫 번째 주문만 가져옴
                    StoreEntity store = order.getStoreEntity();
                    HotelEntity hotel = store.getHotelEntity();
                    BranchEntity branch = hotel.getBranchEntity();
                    CenterEntity center = branch.getCenterEntity();

                    String roomNumber = order.getCheckInEntity() != null ? String.valueOf(order.getCheckInEntity().getRoomEntity()) : null;

                    // DTO 객체 생성 후 반환
                    return new PaymentDTO(
                            payment.getId(),
                            payment.getTotalAmount(),
                            payment.getRegTime(),
                            order.getOrdersStatus().name(),
                            store.getName(),
                            hotel.getName(),
                            branch.getName(),
                            center.getName(),
                            roomNumber,
                            payment.getPaidCheck()
                    );
                })
                .collect(Collectors.toList());
    }*/ // 이거 지우면 안됨 무조건 지우면 안됨 ㄹㅇ 지우면 안됨
    /*
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
*/ // 이거 지우면 안됨 무조건 지우면안됨 ㄹㅇ 지우면 안됨



}
