/*********************************************************************
 * 클래스명 : MembersRepository
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Entity.MembersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MembersRepository extends JpaRepository<MembersEntity, Long> {


    @Query("select m from MembersEntity m where m.role = 'SUPERADMIN'")
    List<MembersEntity> selectSuperAd();

    @Query("select m from MembersEntity m where m.role = 'ADMIN'")
    List<MembersEntity> selectHotelAd();
    @Query("select m from MembersEntity m where m.role = 'STOREADMIN'")
    List<MembersEntity> selectStoreA();

    @Query("select m from MembersEntity m where m.email = :email")
    MembersEntity selectEmail(String email);
    //MembersEntity findByEmail(String email);
}
