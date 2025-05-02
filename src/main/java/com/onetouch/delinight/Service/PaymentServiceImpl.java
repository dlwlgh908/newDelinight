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
import com.onetouch.delinight.DTO.*;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            // todo : 추 후에 플랫폼에 속해있는 총 각 센터 매출 및 하위 매출 순정보
        } else if (role.equals(Role.SUPERADMIN)) {
            paymentDTOList = customPaymentRepository.findPaymentByCriteria(paidCheck, memberId, startDate, endDate);
        } else if (role.equals(Role.ADMIN)) {
            paymentDTOList = customPaymentRepository.findPaymentByCriteria(paidCheck, memberId, startDate, endDate);
        } else if (role.equals(Role.STOREADMIN)) {
            paymentDTOList = customPaymentRepository.findPaymentByCriteria(paidCheck, memberId, startDate, endDate);
        } else {
            throw new IllegalArgumentException("잘못된 권한입니다.");
        }

        return processPayments(paymentDTOList); // 계산된 데이터 반환
    }


    @Override
    public List<ExcelDTO> extractData(List<PaymentDTO> paymentDTOList) {

        List<ExcelDTO> result = new ArrayList<>();


        for(PaymentDTO paymentDTO : paymentDTOList){
            LocalDate date = paymentDTO.getRegTime().toLocalDate();
            for(OrdersDTO ordersDTO : paymentDTO.getOrdersDTOList()){
                for(OrdersItemDTO ordersItemDTO : ordersDTO.getOrdersItemDTOList()){
                    MenuDTO menuDTO = ordersItemDTO.getMenuDTO();
                    ExcelDTO row = new ExcelDTO();
                    row.setDate(date);
                    row.setQuantity(ordersItemDTO.getQuantity().intValue());
                    row.setUnitPrice(menuDTO.getPrice());
                    row.setMenuName(menuDTO.getName());
                    row.setTotalPrice(menuDTO.getPrice()*ordersItemDTO.getQuantity().intValue());
                    row.setHotelName(ordersDTO.getStoreDTO().getHotelDTO().getName());
                    row.setStoreName(ordersDTO.getStoreDTO().getName());
                    result.add(row);
                }
            }
        }
        return result;
    }

    @Override
    public List<PaymentDTO> processPayments(List<PaymentDTO> paymentDTOList) {

        // AtomicInteger 사용하여 최종 금액 합산 값을 변경할 수 있게 처리 (멀티스레드 환경에서 안전)
        AtomicInteger TotalPriceVat = new AtomicInteger(0);  // 최종 금액 합산 (전체 합계금액 + 부가세 합산)

        // 리스트 내 각 PaymentDTO 처리
        return paymentDTOList.stream().map(data -> {

            int totalPrice = 0;   // 합계 금액 (주문 금액 합)
            int unpaid = 0;       // 미결제 금액 (결제가 안 된 주문 금액)
            int vat = 0;          // 부가세 (각 주문의 부가세 합)

            // 각 PaymentDTO에 포함된 주문들에 대해 계산 처리
            for (OrdersDTO order : data.getOrdersDTOList()) {

                Long orderPrice = order.getTotalPrice();    // 주문 금액
                int orderVat = (int) (orderPrice * 0.1);    // 부가세 10% 계산

                // 결제 상태에 따라 미결제 금액과 부가세 계산
                if (!order.isPaid()) {                      // 미결제 상태인 경우
                    unpaid += orderPrice;                   // 미결제 금액 누적
                    vat += orderVat;                        // 부가세 누적
                    totalPrice += orderPrice + orderVat;    // 미결제 금액과 부가세를 합산한 금액
                } else {                                    // 결제 완료된 주문은 금액만 누적
                    totalPrice += orderPrice;               // 결제 완료된 주문의 합계금액만 누적
                }
            }

            // 각 PaymentDTO에 최종 계산된 값 설정
            data.setTotalPrice(totalPrice);                 // 합계 금액 설정
            data.setUnpaid(unpaid);                         // 미결제 금액 설정
            data.setVat(vat);                               // 부가세 설정

            // 각 PaymentDTO에 대해 최종 합계에 부가세 + 합계금액을 더하여 전체 합계에 반영
            int finalTotal = totalPrice + vat;              // 최종 금액 계산 (합계금액 + 부가세)
            TotalPriceVat.addAndGet(finalTotal);            // 전체 총합에 최종 금액 누적

            // 처리된 PaymentDTO 반환
            return data;
        }).collect(Collectors.toList());  // 결과 리스트 반환
    }



}
