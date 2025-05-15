/*********************************************************************
 * 클래스명 : MembersService
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.HotelDTO;
import com.onetouch.delinight.DTO.InquireDTO;
import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.Entity.InquireEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.method.P;

import java.lang.reflect.Member;
import java.util.List;

public interface InquireService{
    //문의사항 등록
    public InquireDTO register(InquireDTO inquireDTO,String email);
    public List<InquireDTO> findUnprocessedInquire(MembersDTO membersDTO);
    //목록
    public Page<InquireDTO> inquireList(Pageable pageable,Long hotelId);
    public Page<InquireDTO> inquireList(Pageable pageable,String email);



    //상세보기
    public InquireDTO read(Long id);
    //수정
    public InquireDTO update(InquireDTO inquireDTO);
    //삭제
    public void delete(Long id);

}
