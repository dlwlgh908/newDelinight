package com.onetouch.delinight.Controller;

import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.Service.MembersService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rest")
@Log4j2
public class MembersRestController {
    private final MembersService membersService;

    @GetMapping("/approveBtn/{id}")
    public void approveBtn(@PathVariable("id") Long id) {

        log.info("changestatus 페이지 진입!!");
        log.info("changestatus 페이지 진입!!");
        log.info("changestatus 페이지 진입!!");
        log.info(id);
        log.info(id);
        log.info(id);
        log.info(id);
        MembersDTO membersDTO =
            membersService.approve(id);
        log.info(membersDTO);
        log.info(membersDTO);
        log.info(membersDTO);
        log.info(membersDTO);


    }
    @GetMapping("/DisapproveBtn/{id}")
    public void DisapproveBtn(@PathVariable("id") Long id) {

        log.info("changestatus 페이지 진입!!");
        log.info("changestatus 페이지 진입!!");
        log.info("changestatus 페이지 진입!!");
        log.info(id);
        log.info(id);
        log.info(id);
        log.info(id);
        MembersDTO membersDTO =
                membersService.Disapprove(id);
        log.info(membersDTO);
        log.info(membersDTO);
        log.info(membersDTO);
        log.info(membersDTO);


    }



//        try {
//            MembersDTO membersDTO = membersService.
//
//            ReplyDTO replyDTO =
//                    replyService.read(rno);
//            return new ResponseEntity<ReplyDTO>(replyDTO, HttpStatus.OK);
//
//        } catch (EntityNotFoundException e) {
//            return new ResponseEntity<String>("게시물이 삭제되었거나 잘못된 경로입니다.", HttpStatus.BAD_REQUEST);
//
//
//        }
//    }


}
