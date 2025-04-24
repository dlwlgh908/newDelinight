package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Constant.PaidCheck;
import com.onetouch.delinight.DTO.*;
import com.onetouch.delinight.Entity.PaymentEntity;
import com.onetouch.delinight.Entity.QOrdersEntity;
import com.onetouch.delinight.Entity.QPaymentEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Log4j2
public class CustomPaymentRepositoryImpl implements CustomPaymentRepository {

    @PersistenceContext
    private EntityManager entityManager;
    private final ModelMapper modelMapper;

    public List<PaymentDTO> findPaymentByCriteria(String priceMonth, String type, Long storeId, Boolean isPaid) {
        QPaymentEntity paymentEntity = QPaymentEntity.paymentEntity;
        QOrdersEntity ordersEntity = QOrdersEntity.ordersEntity;
        BooleanBuilder builder = new BooleanBuilder();

        JPAQuery<PaymentEntity> query = new JPAQuery<>(entityManager);

        log.info("기준에 맞는 결제 조회 쿼리 시작 - priceMonth: {}, storeId: {}, isPaid: {}", priceMonth, storeId, isPaid);

        // 1. from 절 설정
        query.from(paymentEntity).join(paymentEntity.ordersEntityList, ordersEntity).fetchJoin();

       log.info("from과 join 절이 설정된 쿼리 빌드 완료.");

        // 2. 정산 월 필터링
        if (priceMonth != null) {
            builder.and(paymentEntity.priceMonth.eq(priceMonth));
            log.info("priceMonth로 필터링: {}", priceMonth);
        }


        // 3. 매장 필터링
        if (storeId != null) {
            builder.and(ordersEntity.storeEntity.id.eq(storeId));
            log.info("매장 필터 추가: {}", storeId);
        }

        // 4. 정산 상태 필터링
        if (isPaid != null) {
            builder.and(paymentEntity.paidCheck.eq(isPaid ? PaidCheck.paid : PaidCheck.none));
            log.info("정산 상태 필터 추가: {}", isPaid ? PaidCheck.paid : PaidCheck.none);
        }

        query.where(builder);

        // 5. 쿼리 실행
        List<PaymentEntity> paymentEntities = query.fetch();
        log.info("쿼리 실행 완료. 조회된 PaymentEntity 개수: {}", paymentEntities.size());

        // 6. PaymentEntity → PaymentDTO 변환
        List<PaymentDTO> paymentDTOList = paymentEntities.stream().map(payment -> {
            // OrdersDTO 변환
            List<OrdersDTO> ordersDTOList = payment.getOrdersEntityList().stream().map(order -> {

                log.info("OrderEntity (id: {})를 OrdersDTO로 변환 중", order.getId());
                return OrdersDTO.builder()
                        .id(order.getId())
                        .totalPrice(order.getTotalPrice())
                        .ordersStatus(order.getOrdersStatus())
                        .pendingTime(order.getPendingTime())
                        .deliveredTime(order.getDeliveredTime())
                        .storeDTO(modelMapper.map(order.getStoreEntity(), StoreDTO.class)
                        .setHotelDTO(modelMapper.map(order.getStoreEntity().getHotelEntity(), HotelDTO.class)
                        .setBranchDTO(modelMapper.map(order.getStoreEntity().getHotelEntity().getBranchEntity(), BranchDTO.class)
                        .setCenterDTO(modelMapper.map(order.getStoreEntity().getHotelEntity().getBranchEntity().getCenterEntity(), CenterDTO.class)))))
                        .build();
            }).collect(Collectors.toList());

            // PaymentDTO 변환
            log.info("PaymentEntity (id: {})를 PaymentDTO로 변환 중", payment.getId());
            return PaymentDTO.builder()
                    .totalId(payment.getId())                           // 정산 ID
                    .checkPaid(payment.getPaidCheck())                  // 정산 상태
                    .regTime(payment.getRegTime())                      // 등록일
                    .updateTime(payment.getUpdateTime())                // 수정일
                    .ordersDTOList(ordersDTOList)                       // 주문 내역 리스트
                    .build();
        }).collect(Collectors.toList());

        log.info("총 {}개의 PaymentEntities를 PaymentDTO로 변환 완료", paymentDTOList.size());

        return paymentDTOList;
    }





}
