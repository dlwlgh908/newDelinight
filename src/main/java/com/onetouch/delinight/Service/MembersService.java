/*********************************************************************
 * 클래스명 : MembersService
 * 기능 :
 * 작성자 : 이효찬
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.Role;
import com.onetouch.delinight.Constant.Status;
import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.Entity.MembersEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.List;
import java.util.Map;

public interface MembersService {

    public List<MembersDTO> findMembersListByCenterEmail(String email);

    public List<MembersDTO> findMembersListByHotelEmail(String email);

    public boolean assignCheck(String email, int sep);
    public void create(MembersDTO membersDTO);
    public void update(MembersDTO membersDTO);

    public Page<MembersEntity> getList(int page);
    public Page<MembersEntity> getListHotel(int page, String email);
    public Page<MembersEntity> getListStore(int page, String email);
    public Page<MembersEntity> getListBystatus(Status status, int page);

    public List<MembersDTO> findAll();

    //    public List<MembersDTO> findSuper();

    Page<MembersEntity> findAccount(Status status, int page, String email, String sep);


    public Integer countOfRequestAccount(String email);

    public MembersDTO approve(Long id);
    public MembersDTO Disapprove(Long id);

    public Map<Role, Long> findRoleByEmail(String email);
    public Role findOnlyRoleByEmail(String email);

    public MembersDTO findByEmail(String email);

}