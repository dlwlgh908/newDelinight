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
import com.onetouch.delinight.Entity.InquireEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HotelRepository extends JpaRepository<HotelEntity, Long> {

    public HotelEntity findByMembersEntity_Email(String email);

    public boolean existsByMembersEntity_Email(String email);

    @Query("select h from HotelEntity h where h.branchEntity.centerEntity.membersEntity.email = :email")
    public List<HotelEntity> selectallBySuper(String email);








}
