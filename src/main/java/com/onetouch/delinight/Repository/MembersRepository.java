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

public interface MembersRepository extends JpaRepository<MembersEntity, Long> {


    @Query("select m from MembersEntity m where m.role = 'SUPERADMIN'")
    Page<MembersEntity> selectSuperAd(Pageable pageable);

    @Query("select m from MembersEntity m where m.role = 'SUPERADMIN' and m.status = :status")
    Page<MembersEntity> selectSuperAdByStatus(Status status, Pageable pageable);

    @Query("select m from MembersEntity m where m.role = 'ADMIN'")
    Page<MembersEntity> selectHotelAd(Pageable pageable);

    @Query("select m from MembersEntity m where m.role = 'STOREADMIN'")
    Page<MembersEntity> selectStoreAd(Pageable pageable);


    @Query("select m from MembersEntity m where m.role = 'ADMIN' and m.status=:status")
    Page<MembersEntity> selectHotelAdByStatus(Status status, Pageable pageable);

    @Query("select m from MembersEntity m where m.role = 'STOREADMIN' and m.status = :status")
    Page<MembersEntity> selectStoreAdByStatus(Status status, Pageable pageable);

    @Query("select m from MembersEntity m where m.email = :email")
    MembersEntity selectEmail(String email);
    //MembersEntity findByEmail(String email);
    MembersEntity findByEmail(String email);

}
