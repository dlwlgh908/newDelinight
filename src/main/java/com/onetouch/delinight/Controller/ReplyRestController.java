package com.onetouch.delinight.Controller;

import com.onetouch.delinight.DTO.ReplyDTO;
import com.onetouch.delinight.Service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.wiring.BeanWiringInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyRestController {

    private final ReplyService replyService;

    @GetMapping("/list/{qnaId}")
    public ResponseEntity<List<ReplyDTO>> list(@PathVariable("qnaId") Long qnaId){
        log.info("페이지 진입");
        List<ReplyDTO> replyDTOList = replyService.list(qnaId);
        return new ResponseEntity<List<ReplyDTO>>(replyDTOList , HttpStatus.OK);
    }

}
