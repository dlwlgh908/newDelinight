package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Constant.PaidCheck;
import com.onetouch.delinight.Constant.Role;
import com.onetouch.delinight.DTO.*;
import com.onetouch.delinight.Entity.PaymentEntity;
import com.onetouch.delinight.Entity.QMembersEntity;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Log4j2
public class CustomPaymentRepositoryImpl implements CustomPaymentRepository {

    @PersistenceContext
    private EntityManager entityManager;
    private final ModelMapper modelMapper;

    public List<PaymentDTO> findPaymentByCriteria(String priceMonth, String type, Long storeId, Boolean isPaid, String admin) {
        QPaymentEntity paymentEntity = QPaymentEntity.paymentEntity;
        QOrdersEntity ordersEntity = QOrdersEntity.ordersEntity;
        QMembersEntity membersEntity = QMembersEntity.membersEntity;
        BooleanBuilder builder = new BooleanBuilder();

        JPAQuery<PaymentEntity> query = new JPAQuery<>(entityManager);

        log.info("기준에 맞는 결제 조회 쿼리 시작 - priceMonth: {}, storeId: {}, isPaid: {}", priceMonth, storeId, isPaid);

        // 1. from 절 설정
        query.from(paymentEntity).join(paymentEntity.ordersEntityList, ordersEntity).fetchJoin();

        log.info("from과 join 절이 설정된 쿼리 빌드 완료.");

        // 2. 정산 월 필터링
        if (priceMonth != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            LocalDateTime startDate = LocalDateTime.parse(priceMonth + "-1", formatter);
            builder.and(paymentEntity.regTime.month().eq(startDate.getMonthValue()));
        }

        if(type != null) {
            switch (type) {
                case "DAY":
                    builder.and(paymentEntity.regTime.dayOfYear().eq(LocalDate.now))
            }
        }


        // 3. 매장 필터링
        if (storeId != null) {
            query.where(ordersEntity.storeEntity.id.eq(storeId));
            log.info("매장 필터 추가: {}", storeId);
        }

        // 4. 정산 상태 필터링
        if (isPaid != null) {
            query.where(paymentEntity.paidCheck.eq(isPaid ? PaidCheck.paid : PaidCheck.none));
            log.info("정산 상태 필터 추가: {}", isPaid ? PaidCheck.paid : PaidCheck.none);
        }

        // 5. admin 조건 추가
        if (admin != null) {
            // admin 값을 Role 열거 타입으로 변환
            Role role = Role.valueOf(admin.toUpperCase());  // 파라미터를 대소문자 구분 없이 비교
            adminRoleFilter(query, role, paymentEntity, membersEntity);
        }



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
                    .priceMonth(payment.getPriceMonth())                // 정산 월
                    .checkPaid(payment.getPaidCheck())                  // 정산 상태
                    .regTime(payment.getRegTime())                      // 등록일
                    .updateTime(payment.getUpdateTime())                // 수정일
                    .ordersDTOList(ordersDTOList)                       // 주문 내역 리스트
                    .build();
        }).collect(Collectors.toList());

        log.info("총 {}개의 PaymentEntities를 PaymentDTO로 변환 완료", paymentDTOList.size());

        return paymentDTOList;
    }

    private void adminRoleFilter(JPAQuery<PaymentEntity> query, Role role, QPaymentEntity paymentEntity, QMembersEntity membersEntity) {
        query.where(paymentEntity.members.eq(membersEntity));  // 공통적으로 paymentEntity.members를 필터링

        switch (role) {
            case SYSTEMADMIN:
                query.where(membersEntity.role.eq(Role.SYSTEMADMIN));
                log.info("SYS 관리자 유형 필터 추가 : {}", Role.SYSTEMADMIN);
                break;
            case SUPERADMIN:
                query.where(membersEntity.role.eq(Role.SUPERADMIN));
                log.info("SUPER 관리자 유형 필터 추가 : {}", Role.SUPERADMIN);
                break;
            case ADMIN:
                query.where(membersEntity.role.eq(Role.ADMIN));
                log.info("ADMIN 관리자 유형 필터 추가 : {}", Role.ADMIN);
                break;
            case STOREADMIN:
                query.where(membersEntity.role.eq(Role.STOREADMIN));
                log.info("STORE 관리자 유형 필터 추가 : {}", Role.STOREADMIN);
                break;
            default:
                throw new IllegalArgumentException("유효하지 않은 역할: " + role);
        }
    }




}
