/*********************************************************************
 * 클래스명 : MembersService
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.InquireDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InquireService {
    //문의사항 등록
    public InquireDTO register(InquireDTO inquireDTO, Long roomId, Long usersId);
    //목록
    public Page<InquireDTO> inquireList(Pageable pageable, String email);
    public List<InquireDTO> inquireList(Long hotelId);

    //상세보기
    public InquireDTO read(Long id);
    //수정
    public InquireDTO update(InquireDTO inquireDTO);
    //삭제
    public void delete(Long id);




}
