/*********************************************************************
 * 클래스명 : MembersRepository
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Repository;

import com.onetouch.delinight.DTO.QnaDTO;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Entity.QnaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QnaRepository extends JpaRepository<QnaEntity, Long> {

//    public QnaEntity findByCheckInEntity_Id(Long id);




    @Query("select c from CheckInEntity c where c.id = :id")
    public QnaEntity selectId(Long id);

    @Query("select q from QnaEntity q, CheckInEntity c where q.checkInEntity.id = c.id")
    public QnaEntity slelctA(Long id);




}
