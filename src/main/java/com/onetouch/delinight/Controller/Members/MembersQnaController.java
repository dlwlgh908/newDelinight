package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.DTO.QnaDTO;
import com.onetouch.delinight.Repository.QnaRepository;
import com.onetouch.delinight.Service.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/members/qna")
public class MembersQnaController {
    private final QnaService qnaService;
    private final QnaRepository qnaRepository;



    // 멤버에서는 읽기(읽기 페이지 속에 댓글이 들어가는거임), 리스트


    //목록
    @GetMapping("/list")
    public String list(@PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                       Model model, Principal principal){

        String agentEmail = principal.getName();
        Page<QnaDTO> qnaDTOList = qnaService.list(pageable); // 메일을 리스트 함수에 같이 던져서 그걸로 자기 호텔에 관련된 문의사항만 볼 수 있게 수정이 필요함
        log.info(qnaDTOList.getPageable().getPageNumber());
        model.addAttribute("qnaDTOList",qnaDTOList);
        log.info(qnaDTOList.getContent());

        return "/members/qna/list";
    }



    //상세보기
    @GetMapping("/read")
    public String read(@RequestParam Long id, Model model, Principal principal, RedirectAttributes redirectAttributes){
        QnaDTO qnaDTO = qnaService.read(id);
        model.addAttribute("qnaDTO",qnaDTO);
        return "/members/qna/read";
    }



}
