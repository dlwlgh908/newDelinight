/*********************************************************************
 * 클래스명 : MembersRepository
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Entity.HotelEntity;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

    public StoreEntity findByMembersEntity_Email(String email);

    public List<StoreEntity> findByHotelEntity_Id(Long hotelId);

    @Query("select s from StoreEntity s where s.hotelEntity.membersEntity.email = :email")
    public List<StoreEntity> selectallByHotelAdmin(String email);


}
