/*********************************************************************
 * 클래스명 : MembersRepository
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    PaymentEntity findByOrdersEntityList_Id(Long id);

    @Query("""
        select p from PaymentEntity p
        join p.ordersEntityList o
        join o.storeEntity s
        join s.hotelEntity h
        join h.branchEntity b
        join b.centerEntity c
        where c.id = :centerId""")
        List<PaymentEntity> findCenterForDate(@Param("centerId") Long centerId);   // centerId로 하위 조회 후 정산

    @Query("""
           select p from PaymentEntity p
           join p.ordersEntityList o
           join o.storeEntity s
           join s.hotelEntity h
           join h.branchEntity b
           where b.id = :branchId""")
    List<PaymentEntity> findBranchForDate(@Param("branchId") Long branchId);       // branchId로 하위 조회 후 정산

    @Query("""
            select p from PaymentEntity p
            join p.ordersEntityList o
            join o.storeEntity s
            join s.hotelEntity h
            where  h.id = :hotelId""")
    List<PaymentEntity> findHotelForDate(@Param("hotelId") Long hotelId);          // hotelId로 하위 조회 후 정산

    @Query("""
            select p from PaymentEntity p
            join p.ordersEntityList o
            join o.storeEntity s
            where s.id = :storeId""")
    List<PaymentEntity> findStoreForDate(@Param("storeId") Long storeId);           // storeId로 하위 조회 후 정산

}
