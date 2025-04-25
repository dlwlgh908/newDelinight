/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.PaidCheck;
import com.onetouch.delinight.Constant.Role;
import com.onetouch.delinight.DTO.CheckInDTO;
import com.onetouch.delinight.DTO.OrdersDTO;
import com.onetouch.delinight.DTO.PaymentDTO;
import com.onetouch.delinight.DTO.StoreDTO;
import com.onetouch.delinight.Entity.OrdersEntity;
import com.onetouch.delinight.Entity.PaymentEntity;
import com.onetouch.delinight.Repository.CustomPaymentRepositoryImpl;
import com.onetouch.delinight.Repository.MembersRepository;
import com.onetouch.delinight.Repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final CustomPaymentRepositoryImpl customPaymentRepository;
    private final PaymentRepository paymentRepository;
    private final MembersRepository membersRepository;
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
    public List<PaymentDTO> paymentByCriteria (PaidCheck paidCheck, Long memberId, LocalDate startDate, LocalDate endDate) {

        Role role = membersRepository.findById(memberId).get().getRole();

        List<PaymentDTO> paymentDTOList = new ArrayList<>();

        if (role.equals(Role.SYSTEMADMIN)) {
            log.info("추 후 플랫폼에 속해 있는 매출을 볼 수 있게 할 예정");
        } else if (role.equals(Role.SUPERADMIN)) {
            log.info("슈퍼 어드민은 전체 매출과 호텔별 매출 조회");

            paymentDTOList = customPaymentRepository.findPaymentByCriteria(paidCheck, memberId, startDate, endDate);
        } else if (role.equals(Role.ADMIN)) {
            log.info("호텔 어드미은 전체 매출과 스토어별 매출 조회");
            paymentDTOList = customPaymentRepository.findPaymentByCriteria(paidCheck, memberId, startDate, endDate);
        } else if (role.equals(Role.STOREADMIN)) {
            log.info("스토어 어드민은 자신이 속한 매장의 메뉴 매출 조회");
            paymentDTOList = customPaymentRepository.findPaymentByCriteria(paidCheck, memberId, startDate, endDate);
        } else {
            log.info("알 수 없는 권한 : {}", role);
            throw new IllegalArgumentException("잘못된 권한입니다.");
        }

        // 후처리 및 계산
        List<PaymentDTO> processedList = processPayments(paymentDTOList);

        log.info("계산된 결제 내역 처리 완료");

        return processedList; // 계산된 데이터 반환
    }


    @Override
    public List<PaymentDTO> processPayments(List<PaymentDTO> paymentDTOList) {
        log.info("후처리 시작 - 총 {}개의 PaymentDTO가 전달되었습니다.", paymentDTOList.size());

        // AtomicInteger 사용하여 최종 금액 합산 값을 변경할 수 있게 처리 (멀티스레드 환경에서 안전)
        AtomicInteger TotalPriceVat = new AtomicInteger(0);  // 최종 금액 합산 (전체 합계금액 + 부가세 합산)

        // 리스트 내 각 PaymentDTO 처리
        return paymentDTOList.stream().map(data -> {
            log.info("처리 중인 PaymentDTO - ID: {}", data.getTotalId());

            int totalPrice = 0;   // 합계 금액 (주문 금액 합)
            int unpaid = 0;       // 미결제 금액 (결제가 안 된 주문 금액)
            int vat = 0;          // 부가세 (각 주문의 부가세 합)

            // 각 PaymentDTO에 포함된 주문들에 대해 계산 처리
            for (OrdersDTO order : data.getOrdersDTOList()) {
                log.info("처리 중인 주문 - ID: {}, 금액: {}", order.getId(), order.getTotalPrice());

                Long orderPrice = order.getTotalPrice();  // 주문 금액
                int orderVat = (int) (orderPrice * 0.1);  // 부가세 10% 계산

                // 결제 상태에 따라 미결제 금액과 부가세 계산
                if (!order.isPaid()) {                      // 미결제 상태인 경우
                    unpaid += orderPrice;                   // 미결제 금액 누적
                    vat += orderVat;                        // 부가세 누적
                    totalPrice += orderPrice + orderVat;    // 미결제 금액과 부가세를 합산한 금액
                    log.info("미결제 금액 추가됨 - 미결제 금액: {}, 부가세: {}, 합계금액 누적: {}", orderPrice, orderVat, totalPrice);
                } else {                                    // 결제 완료된 주문은 금액만 누적
                    totalPrice += orderPrice;               // 결제 완료된 주문의 합계금액만 누적
                }
            }

            // 각 PaymentDTO에 최종 계산된 값 설정
            data.setTotalPrice(totalPrice);  // 합계 금액 설정
            data.setUnpaid(unpaid);          // 미결제 금액 설정
            data.setVat(vat);                // 부가세 설정

            // 각 PaymentDTO에 대해 최종 합계에 부가세 + 합계금액을 더하여 전체 합계에 반영
            int finalTotal = totalPrice + vat;      // 최종 금액 계산 (합계금액 + 부가세)
            TotalPriceVat.addAndGet(finalTotal);    // 전체 총합에 최종 금액 누적

            log.info("PaymentDTO 후처리 완료 - ID: {}, 합계 금액: {}, 미결제 금액: {}, 부가세: {}, 최종 금액: {}", data.getTotalId(), totalPrice, unpaid, vat, finalTotal);

            // 처리된 PaymentDTO 반환
            return data;
        }).collect(Collectors.toList());  // 결과 리스트 반환
    }



}
