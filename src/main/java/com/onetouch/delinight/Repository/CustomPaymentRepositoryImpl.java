package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Constant.PaidCheck;
import com.onetouch.delinight.DTO.*;
import com.onetouch.delinight.Entity.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Log4j2
public class CustomPaymentRepositoryImpl implements CustomPaymentRepository {

    @PersistenceContext
    private EntityManager entityManager;
    private final ModelMapper modelMapper;

    public List<PaymentDTO> findPaymentByCriteria(String priceMonth, Long storeId, Boolean isPaid, Long memberId) {
        QMembersEntity membersEntity = QMembersEntity.membersEntity;
        QPaymentEntity paymentEntity = QPaymentEntity.paymentEntity;
        QOrdersEntity ordersEntity = QOrdersEntity.ordersEntity;
        QOrdersItemEntity ordersItemEntity = QOrdersItemEntity.ordersItemEntity;
        QMenuEntity menuEntity = QMenuEntity.menuEntity;
        BooleanBuilder builder = new BooleanBuilder();

        JPAQuery<PaymentEntity> query = new JPAQuery<>(entityManager);

        log.info("기준에 맞는 결제 조회 쿼리 시작 - priceMonth: {}, storeId: {}, isPaid: {}", priceMonth, storeId, isPaid);

        // 1. from 절 설정
        query.from(paymentEntity)
                .join(paymentEntity.ordersEntityList, ordersEntity).fetchJoin()
                .join(ordersEntity.ordersItemEntities, ordersItemEntity)
                .join(ordersItemEntity.menuEntity, menuEntity)
                .fetchJoin();
       log.info("from과 join 절이 설정된 쿼리 빌드 완료.");

        // 2. 멤버 ID 필터링 추가
        if (memberId != null) {
            query.join(ordersEntity.storeEntity.membersEntity, membersEntity)   // PaymentEntity에 있는 membersEntity와 join
                    .where(membersEntity.id.eq(memberId));                      // 멤버 ID로 필터링
            log.info("멤버 ID로 필터링 : {}", memberId);
        }

        // 3. priceMonth 필터링 (regTime과 비교)
        if (priceMonth != null) {

            LocalDate startDate = null;
            LocalDate endDate = null;
            int year = LocalDate.now().getYear();
            int month = Integer.parseInt(priceMonth);
            startDate = LocalDate.of(year, month, 1);
            endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
            log.info("priceMonth로 필터링: {} - Start: {} End: {}", priceMonth, startDate, endDate);

            // LocalDateTime으로 변환 후 between 조건 적용
            builder.and(paymentEntity.regTime.between(
                    startDate.atStartOfDay(),                        // LocalDate에서 LocalDateTime으로 변환
                    endDate.atTime(23, 59, 59)   // LocalDate에서 LocalDateTime으로 변환
            ));

        }


        // 3. 매장 필터링
        if (storeId != null) {
            builder.and(ordersEntity.storeEntity.id.eq(storeId));
            log.info("매장 필터 추가: {}", storeId);
        }

        // 5. 정산 상태 필터링
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
                        // OrderItemDTO 변환 추가
                        .ordersItemDTOList(order.getOrdersItemEntities().stream().map(orderItem -> {
                            log.info("OrderItemEntity (id: {})를 OrderItemDTO로 변환 중", orderItem.getId());
                            // OrderItemDTO에 MenuDTO 포함
                            return OrdersItemDTO.builder()
                                    .id(orderItem.getId())
                                    .quantity(orderItem.getQuantity())
                                    .menuDTO(modelMapper.map(orderItem.getMenuEntity(), MenuDTO.class))  // MenuDTO 추가
                                    .build();
                        }).collect(Collectors.toList()))  // 추가된 orderItemDTOList
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
