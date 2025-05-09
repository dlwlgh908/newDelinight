package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.ReplyDTO;
import com.onetouch.delinight.Entity.ReplyEntity;

import java.util.List;

public interface ReplyService {
    //댓글 등록
    public ReplyDTO register(ReplyDTO replyDTO);
    //댓글 목록
    public List<ReplyDTO> findAll();
    public List<ReplyDTO> list(Long id);

    //상세보기
    public ReplyDTO read(Long id);
    public ReplyDTO findByInquireId(Long id);

    //댓글 수정
    public  ReplyDTO update(ReplyDTO replyDTO);

}
