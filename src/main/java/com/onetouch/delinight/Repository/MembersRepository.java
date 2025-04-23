/*********************************************************************
 * 클래스명 : MembersRepository
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Constant.Status;
import com.onetouch.delinight.Entity.MembersEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MembersRepository extends JpaRepository<MembersEntity, Long> {


    @Query("select m from MembersEntity m where m.role = 'SUPERADMIN'")
    Page<MembersEntity> selectSuperAd(Pageable pageable);

    @Query("select m from MembersEntity m where m.role = 'SUPERADMIN' and m.status = :status")
    Page<MembersEntity> selectSuperAdByStatus(Status status, Pageable pageable);

    @Query("select m from MembersEntity m where m.role = 'ADMIN' and m.centerEntity.id = :id")
    Page<MembersEntity> selectHotelAd(Pageable pageable, Long id);

    @Query("select m from MembersEntity m where m.role = 'STOREADMIN' and m.hotelEntity.id = :id")
    Page<MembersEntity> selectStoreAd(Pageable pageable, Long id);


    @Query("select m from MembersEntity m where m.role = 'ADMIN' and m.status=:status and m.centerEntity.id =:id")
    Page<MembersEntity> selectHotelAdByStatus(Status status, Pageable pageable, Long id);

    @Query("select m from MembersEntity m where m.role = 'STOREADMIN' and m.status = :status and m.hotelEntity.id =:id")
    Page<MembersEntity> selectStoreAdByStatus(Status status, Pageable pageable,Long id);

    @Query("select m from MembersEntity m where m.email = :email")
    MembersEntity selectEmail(String email);
    //MembersEntity findByEmail(String email);
    MembersEntity findByEmail(String email);

    @Query("select m from MembersEntity m where m.email = :email")
    Optional<MembersEntity> findByEmail2(String email);

}
