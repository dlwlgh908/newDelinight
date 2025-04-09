/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;


import com.onetouch.delinight.DTO.QnaDTO;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class QnaServiceImpl implements QnaService{

    @Override
    public QnaDTO register(QnaDTO qnaDTO){
        return null;
    };
}
