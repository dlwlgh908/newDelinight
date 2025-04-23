package com.onetouch.delinight.Service;

import com.onetouch.delinight.Repository.InquireRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
class InquireServiceImplTest {
    @Autowired
    InquireService inquireService;
    @Autowired
    InquireRepository inquireRepository;


    /*@Test
    public void register(){
        InquireDTO inquireDTO = new InquireDTO();
        inquireDTO.setTitle("605호");
        inquireDTO.setContent("이불 1개 주세요");
        inquireService.register(inquireDTO, inquireDTO.getId());
    }*/

   /* @Test
    public void read(){
        Long id = 1L;

        InquireDTO inquireDTO = inquireService.read(id);
        log.info(inquireDTO);
    }
    @Test
    public void update(){
        InquireEntity inquireEntity = inquireRepository.findById(1L).get();
        inquireEntity.setTitle("603호");
        inquireEntity.setContent("수건 1개 주세요");
        InquireEntity result = inquireRepository.save(inquireEntity);
    }
    @Test
    public void del(){
        inquireService.delete(2L);
    }

*/



}