package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Repository.MembersRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class MembersServiceImplTest {

    @Autowired
    MembersService membersService;
    @Autowired
    MembersRepository membersRepository;


    @Test
    public void createTest(){
        MembersDTO membersDTO = new MembersDTO();
        membersDTO.setName("이효찬");
        membersDTO.setEmail("harry@naver.com");
        membersDTO.setPhone("01041634202");

        membersService.create(membersDTO);
    }

    @Test
    public void listTest(){

        List<MembersDTO> membersDTOList =
            membersService.findAll();
        membersDTOList.forEach(member -> log.info(member));


    }



}