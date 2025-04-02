/*********************************************************************
 * 클래스명 : MembersService
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.MembersDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MembersService {

    public void create(MembersDTO membersDTO);

    public Page<MembersDTO> list(Pageable pageable);

    public List<MembersDTO> findAll();

    public MembersDTO changeStatus(Long id);

}
