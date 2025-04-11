package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.QnaDTO;
import com.onetouch.delinight.DTO.ReplyDTO;
import com.onetouch.delinight.Entity.QnaEntity;
import com.onetouch.delinight.Entity.ReplyEntity;
import com.onetouch.delinight.Repository.QnaRepository;
import com.onetouch.delinight.Repository.ReplyRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Log4j2
@Transactional
class ReplyServiceImplTest {
    @Autowired
    ReplyRepository replyRepository;
    @Autowired
    QnaRepository qnaRepository;
    @Autowired
    ReplyService replyService;
    @Autowired
    ModelMapper modelMapper;

    @Test
    @Commit
    public void register(){


        QnaEntity qnaEntity =
        qnaRepository.findById(1L).orElseThrow(EntityNotFoundException::new);
        log.info("값 :" + qnaEntity);
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setId(1L);
        replyDTO.setReplyText("확인");
        replyDTO.setReplyer("신라호텔");
        log.info(replyDTO);

        QnaDTO qnaDTO =
        modelMapper.map(qnaEntity, QnaDTO.class);
        replyDTO.setQnaDTO(qnaDTO);
        log.info(qnaDTO);

//        ReplyDTO replyDTO = new ReplyDTO();
//        replyDTO.setId(1L);
//        replyDTO.setReplyText("댓글");
//        replyDTO.setRepyler("신라호텔");
//        log.info(replyDTO);

        replyService.register(replyDTO);


    }

    @Test
    @Commit
    public void regiA(){
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setReplyText("확인");
        replyDTO.setReplyer("홍길동");

        QnaEntity qnaEntity = qnaRepository.findById(11L).get();


        log.info(replyDTO);

        ReplyEntity replyEntity = replyService.registerA(replyDTO);
        log.info(replyEntity);
        replyEntity.setQnaEntity(qnaEntity);

        log.info(replyRepository.save(replyEntity));


        log.info(replyDTO);

    }


}