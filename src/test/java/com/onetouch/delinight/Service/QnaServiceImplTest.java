package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.InquireDTO;
import com.onetouch.delinight.Entity.QnaEntity;
import com.onetouch.delinight.Repository.InquireRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
class QnaServiceImplTest {
    @Autowired
    InquireService inquireService;
    @Autowired
    InquireRepository inquireRepository;


//    @Test
//    public void register(){
//        QnaDTO qnaDTO = new QnaDTO();
//        qnaDTO.setTitle("605호");
//        qnaDTO.setContent("이불 1개 주세요");
//        qnaService.register(qnaDTO, qnaDTO.getId());
//    }
    @Test
    public void read(){
        Long id = 1L;

        InquireDTO inquireDTO = inquireService.read(id);
        log.info(inquireDTO);
    }
    @Test
    public void update(){
        QnaEntity qnaEntity = inquireRepository.findById(1L).get();
        qnaEntity.setTitle("603호");
        qnaEntity.setContent("수건 1개 주세요");
        QnaEntity result = inquireRepository.save(qnaEntity);
    }
    @Test
    public void del(){
        inquireService.delete(2L);
    }





}