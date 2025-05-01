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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Log4j2
public class CustomPaymentRepositoryImpl implements CustomPaymentRepository {

    @PersistenceContext
    private EntityManager entityManager;
    private final ModelMapper modelMapper;

    public List<PaymentDTO> findPaymentByCriteria(PaidCheck paidCheck, Long memberId, LocalDate startDate, LocalDate endDate) {
        QMembersEntity membersEntity = QMembersEntity.membersEntity;
        QCenterEntity centerEntity = QCenterEntity.centerEntity;
        QBranchEntity branchEntity = QBranchEntity.branchEntity;
        QHotelEntity hotelEntity = QHotelEntity.hotelEntity;
        QStoreEntity storeEntity = QStoreEntity.storeEntity;
        QPaymentEntity paymentEntity = QPaymentEntity.paymentEntity;
        QOrdersEntity ordersEntity = QOrdersEntity.ordersEntity;
        QOrdersItemEntity ordersItemEntity = QOrdersItemEntity.ordersItemEntity;
        QMenuEntity menuEntity = QMenuEntity.menuEntity;
        BooleanBuilder builder = new BooleanBuilder();

        JPAQuery<PaymentEntity> query = new JPAQuery<>(entityManager);

        log.info("기준에 맞는 결제 조회 쿼리 시작 - isPaid: {}",paidCheck);

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


        // 3. 정산 상태 필터링
        if (paidCheck != null) {
            switch (paidCheck){
                case paid : builder.and(paymentEntity.paidCheck.eq(paidCheck));
                break;
                case none : builder.and(paymentEntity.paidCheck.eq(PaidCheck.none));
                break;
                case both :
                    break;
            }
        }

        // 4. 날짜로 필터링
        if (startDate != null && endDate != null) {
            LocalDateTime startDate1 = startDate.atTime(LocalTime.MIDNIGHT);
            LocalDateTime endDate1 = endDate.atTime(LocalTime.MIDNIGHT);

            builder.and(paymentEntity.regTime.between(startDate1, endDate1));
            log.info("날짜 범위로 필터링: {} ~ {}", startDate, endDate);
        }

        query.where(builder);

        // 5. 쿼리 실행
        List<PaymentEntity> paymentEntities = query.fetch();

        // 6. PaymentEntity → PaymentDTO 변환
        List<PaymentDTO> paymentDTOList = paymentEntities.stream().map(payment -> {
            // OrdersDTO 변환
            List<OrdersDTO> ordersDTOList = payment.getOrdersEntityList().stream()
                    .filter(Objects::nonNull)
                    .distinct()
                    .map(order -> OrdersDTO.builder()
                            .id(order.getId())
                            .totalPrice(order.getTotalPrice())
                            .ordersStatus(order.getOrdersStatus())
                            .pendingTime(order.getPendingTime())
                            .deliveredTime(order.getDeliveredTime())
                            .ordersItemDTOList(order.getOrdersItemEntities().stream().map(orderItem -> OrdersItemDTO.builder()
                                    .id(orderItem.getId())
                                    .quantity(orderItem.getQuantity())
                                    .menuDTO(modelMapper.map(orderItem.getMenuEntity(), MenuDTO.class))
                                    .build()
                            ).collect(Collectors.toList()))
                            .storeDTO(modelMapper.map(order.getStoreEntity(), StoreDTO.class)
                                    .setHotelDTO(modelMapper.map(order.getStoreEntity().getHotelEntity(), HotelDTO.class)
                                            .setBranchDTO(modelMapper.map(order.getStoreEntity().getHotelEntity().getBranchEntity(), BranchDTO.class)
                                                    .setCenterDTO(modelMapper.map(order.getStoreEntity().getHotelEntity().getBranchEntity().getCenterEntity(), CenterDTO.class)))))
                            .build()).toList();

            // PaymentDTO 변환
            return PaymentDTO.builder()
                    .totalId(payment.getId())                           // 정산 ID
                    .checkPaid(payment.getPaidCheck())                  // 정산 상태
                    .regTime(payment.getRegTime())                      // 등록일
                    .ordersDTOList(ordersDTOList)                       // 주문 내역 리스트
                    .build();
        }).collect(Collectors.toList());

        return paymentDTOList;
    }




}
