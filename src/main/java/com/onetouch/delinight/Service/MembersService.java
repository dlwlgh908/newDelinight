/*********************************************************************
 * 클래스명 : MembersService
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.Role;
import com.onetouch.delinight.DTO.MembersDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.List;
import java.util.Map;

public interface MembersService {

    public void create(MembersDTO membersDTO);

    public void hoteladcreate(MembersDTO membersDTO);
    public void storeadcreate(MembersDTO membersDTO);

    public Page<MembersDTO> list(Pageable pageable);

    public List<MembersDTO> findAll();

    public String login(String email, String password);

    public List<MembersDTO> findSuper();
    public List<MembersDTO> findHotelAd();
    public List<MembersDTO> findStoreAd();

    public MembersDTO approve(Long id);
    public MembersDTO Disapprove(Long id);

    public Map<Role, Long> findRoleByEmail(String email);

}
