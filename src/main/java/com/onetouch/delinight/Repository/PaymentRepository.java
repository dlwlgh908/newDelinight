/*********************************************************************
 * 클래스명 : MembersRepository
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Constant.OrderType;
import com.onetouch.delinight.Constant.PaidCheck;
import com.onetouch.delinight.Entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    PaymentEntity findByOrdersEntityList_Id(Long id);

    // 조건 1: 주문이 속한 스토어의 ID가 storeID와 같아야 함(스토어 어드민 전용 조건)
    // 조건 2: 결제 생성일(regTime)이 시작일~종료일 사이여야 함
    // 조건 3: 결제 타입이 선결제인지 후결제인지 확인 (OrderType enum 사용)
    // 조건 4: 정산 여부가 정산(PAID)인지 미정산(UNPAID)인지 구분 (PaidCheck enum 사용)
    @Query("""
    SELECT p 
    FROM PaymentEntity p 
    JOIN p.ordersEntityList o 
    WHERE o.storeEntity.id = :storeId 
    AND p.regTime BETWEEN :startDate AND :endDate 
    AND p.orderType = :orderType 
    AND p.paidCheck = :paidCheck
    """)
    public List<PaymentEntity> findPaymentsForSettlement(@Param("storeId") Long storeId, @Param("startDate")LocalDateTime startDate, @Param("endDate")LocalDateTime endDate, @Param("orderType") OrderType orderType, @Param("paidCheck") PaidCheck paidCheck);


    PaidCheck paidCheck(PaidCheck paidCheck);
}
