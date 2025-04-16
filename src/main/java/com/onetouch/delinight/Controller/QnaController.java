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
import java.util.List;
import java.util.Map;

import static com.onetouch.delinight.Util.PagenationUtil.Pagination;

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

        return "qna/register";
    }
    //등록post
    @PostMapping("/register")
    public String registerPost(QnaDTO qnaDTO,Principal principal){
        if (principal == null) {
            // 익명 사용자의 경우 처리
            System.out.println("로그인 안 됨. 익명으로 처리");
        } else {
            String loginId = principal.getName();
            System.out.println("로그인 사용자: " + loginId);
        }
        Long id = 12L;
        qnaService.register(qnaDTO,id);
        return "redirect:/qna/list";
    }
    //목록
    @GetMapping("/list")
    public String list(@PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                       Model model){
        Page<QnaDTO> qnaDTOList = qnaService.list(pageable);
        log.info(qnaDTOList.getPageable().getPageNumber());
        model.addAttribute("qnaDTOList",qnaDTOList);
        log.info(qnaDTOList.getContent());

        return "qna/list";
    }



    //상세보기
    @GetMapping("/read")
    public String read(@RequestParam Long id, Model model, Principal principal, RedirectAttributes redirectAttributes){
        QnaDTO qnaDTO = qnaService.read(id);
        model.addAttribute("qnaDTO",qnaDTO);
        return "qna/read";
    }
    //수정get
    @GetMapping("/update/{id}")
    public String update(@PathVariable(name = "id")Long id,Model model){
        log.info("수정" +id);
        QnaDTO qnaDTO = qnaService.read(id);
        model.addAttribute("qnaDTO",qnaDTO);
        return "qna/update";
    }
    //수정post
    @PostMapping("/update")
    public String updatePost(QnaDTO qnaDTO){
        log.info(qnaDTO);
        qnaService.update(qnaDTO);
        return "redirect:/qna/read?id="+qnaDTO.getId();
    }
    //삭제
    @GetMapping("/delete")
    public String deleteGET(Long id){
        qnaService.delete(id);
        return "redirect:/qna/list";
    }










}
