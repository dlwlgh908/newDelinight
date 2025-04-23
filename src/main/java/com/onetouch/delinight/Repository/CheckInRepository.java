/*********************************************************************
 * 클래스명 : MembersRepository
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Constant.CheckInStatus;
import com.onetouch.delinight.Entity.CheckInEntity;
import com.onetouch.delinight.Entity.MembersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CheckInRepository extends JpaRepository<CheckInEntity, Long> {

    public CheckInEntity findByRoomEntity_Id(Long id);

    @Query("select c from CheckInEntity c where  c.checkInStatus = :checkinstatus")
    List<CheckInEntity> selectCheckByStatus(CheckInStatus checkinstatus);

    public CheckInEntity findByGuestEntity_Phone(String phone);



    public CheckInEntity findByUsersEntity_Email(String email);




}
