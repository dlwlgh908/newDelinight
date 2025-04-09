package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.QnaDTO;
import com.onetouch.delinight.Entity.QnaEntity;
import com.onetouch.delinight.Repository.QnaRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Log4j2
class QnaServiceImplTest {
    @Autowired
    QnaService qnaService;
    @Autowired
    QnaRepository qnaRepository;


    @Test
    public void register(){
        QnaDTO qnaDTO = new QnaDTO();
        qnaDTO.setTitle("605호");
        qnaDTO.setContent("이불 1개 주세요");
        qnaService.register(qnaDTO);
    }
    @Test
    public void read(){
        Long id = 1L;
        QnaDTO qnaDTO = qnaService.read(id);
        log.info(qnaDTO);
    }
    @Test
    public void update(){
        QnaEntity qnaEntity = qnaRepository.findById(1L).get();
        qnaEntity.setTitle("603호");
        qnaEntity.setContent("수건 1개 주세요");
        QnaEntity result = qnaRepository.save(qnaEntity);
    }
    @Test
    public void del(){
        qnaService.delete(2L);
    }


}