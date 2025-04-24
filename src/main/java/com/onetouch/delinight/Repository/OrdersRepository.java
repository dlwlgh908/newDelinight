/*********************************************************************
 * 클래스명 : MembersRepository
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Constant.OrdersStatus;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Entity.MenuEntity;
import com.onetouch.delinight.Entity.OrdersEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrdersRepository extends JpaRepository<OrdersEntity, Long> {


    OrdersEntity findByCheckInEntity_Id(Long id);
    void deleteByCheckInEntity_Id(Long id);
    Integer countByStoreEntityIdAndOrdersStatus(Long storeId, OrdersStatus status);

    List<OrdersEntity> findByCheckInEntity_UsersEntityEmail(String email);
    Page<OrdersEntity> findByStoreEntity_MembersEntity_EmailAndOrdersStatusNotAndOrdersStatusIsNot(String email, OrdersStatus ordersStatus, OrdersStatus ordersStatus2, Pageable pageable);
    Page<OrdersEntity> findByStoreEntity_MembersEntity_EmailAndOrdersStatusIs(String email, OrdersStatus ordersStatus, Pageable pageable    );

}
