package com.onetouch.delinight.Controller;

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
@RequestMapping("/qna")
public class QnaController {
    private final QnaService qnaService;
    private final QnaRepository qnaRepository;

    //등록get
    @GetMapping("/register")
    public String register(QnaDTO qnaDTO, Model model){
        System.out.println("qnaDTO" + qnaDTO);
        model.addAttribute("qnaDTO" ,qnaDTO);

        return "/qna/register";
    }
    //등록post
    @PostMapping("/register")
    public String registerPost(QnaDTO qnaDTO,Principal principal){

        Long id = 12L;

        qnaService.register(qnaDTO,id);
        return "redirect:/qna/list";
    }
    //목록
    @GetMapping("/list")
    public String list(Model model, @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, Principal principal ){
        Page<QnaDTO> qnaDTOList = qnaService.qnaList(pageable);
        log.info(qnaDTOList.getPageable().getPageNumber());
        model.addAttribute("qnaDTOList",qnaDTOList);
        log.info(qnaDTOList.getContent());
        return "/qna/list";
    }
    //상세보기
    @GetMapping("/read")
    public String read(@RequestParam Long id, Model model, Principal principal, RedirectAttributes redirectAttributes){
        QnaDTO qnaDTO = qnaService.read(id);
        model.addAttribute("qnaDTO",qnaDTO);
        return "/qna/read";
    }
    //수정get
    @GetMapping("/update/{id}")
    public String update(@PathVariable(name = "id")Long id,Model model){
        log.info("수정" +id);
        QnaDTO qnaDTO = qnaService.read(id);
        model.addAttribute("qnaDTO",qnaDTO);
        return "/qna/update";
    }
    //수정post
    @PostMapping("/update")
    public String updatePost(QnaDTO qnaDTO){
        log.info(qnaDTO);
        qnaService.update(qnaDTO);
        return "redirect:/qna/read?id="+qnaDTO.getId();
    }
    //삭제
}
