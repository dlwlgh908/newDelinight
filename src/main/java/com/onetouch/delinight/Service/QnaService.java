/*********************************************************************
 * 클래스명 : MembersService
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.QnaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QnaService {
    //문의사항 등록
    public QnaDTO register(QnaDTO qnaDTO,Long id);
    //목록
    public Page<QnaDTO> qnaList(Pageable pageable);
    //상세보기
    public QnaDTO read(Long id);
    //수정
    public QnaDTO update(QnaDTO qnaDTO);
    //삭제
    public void delete(Long id);




}
