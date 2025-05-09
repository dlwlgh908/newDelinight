/*********************************************************************
 * 클래스명 : MembersRepository
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Constant.OrdersStatus;
import com.onetouch.delinight.DTO.UsersDTO;
import com.onetouch.delinight.Entity.OrdersEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrdersRepository extends JpaRepository<OrdersEntity, Long> {



    List<OrdersEntity> findByCheckInEntity_Id(Long id);
    void deleteByCheckInEntity_Id(Long id);
    Integer countByStoreEntityIdAndOrdersStatus(Long storeId, OrdersStatus status);

    List<OrdersEntity> findByCheckInEntity_UsersEntityEmail(String email);
    List<OrdersEntity> findByCheckInEntity_GuestEntityPhone(String phone);
    Page<OrdersEntity> findByStoreEntity_MembersEntity_EmailAndOrdersStatusNotAndOrdersStatusIsNot(String email, OrdersStatus ordersStatus, OrdersStatus ordersStatus2, Pageable pageable);
    Page<OrdersEntity> findByStoreEntity_MembersEntity_EmailAndOrdersStatusIs(String email, OrdersStatus ordersStatus, Pageable pageable    );

    List<OrdersEntity> findByCheckOutLogEntity_Id(Long checkOutId);

//    @Query("select sum(o.totalPrice) from OrdersEntity o where o.checkInEntity.id = :id")
//    Long selectPriceByCheckinId(@Param("id") Long id);

    @Query("select o from OrdersEntity o where o.checkInEntity.id = :id")
    OrdersEntity selectCheckinId(Long id);



    //사용자의 결제 이력을 가져오는 메서드 (체크인 정보에서 사용자 ID로 조회)
    List<OrdersEntity> findByCheckInEntity_UsersEntity_Id(Long id);






}
