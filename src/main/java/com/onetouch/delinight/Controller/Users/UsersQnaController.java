package com.onetouch.delinight.Controller.Users;

import com.onetouch.delinight.DTO.QnaDTO;
import com.onetouch.delinight.Entity.CheckInEntity;
import com.onetouch.delinight.Entity.HotelEntity;
import com.onetouch.delinight.Entity.QnaEntity;
import com.onetouch.delinight.Entity.UsersEntity;
import com.onetouch.delinight.Repository.CheckInRepository;
import com.onetouch.delinight.Repository.QnaRepository;
import com.onetouch.delinight.Repository.UsersRepository;
import com.onetouch.delinight.Service.CheckInService;
import com.onetouch.delinight.Service.MembersService;
import com.onetouch.delinight.Service.QnaService;
import jakarta.transaction.Transactional;
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
@RequestMapping("/users/qna")
public class UsersQnaController {
    private final QnaService qnaService;
    private final QnaRepository qnaRepository;
    private final MembersService membersService;
    private final UsersRepository usersRepository;
    private final CheckInRepository checkInRepository;
    private final CheckInService checkInService;

    //등록get
    @GetMapping("/register")
    public String register(QnaDTO qnaDTO){


        return "/users/qna/register";
    }
    //등록post
    @PostMapping("/register")
    public String registerPost(QnaDTO qnaDTO,Principal principal){

//        if (principal == null) {
//            // 익명 사용자의 경우 처리
//            System.out.println("로그인 안 됨. 익명으로 처리");
//        } else {
//            String loginId = principal.getName();
//            System.out.println("로그인 사용자: " + loginId);
//        }
//        Long id = 12L;
//        qnaService.register(qnaDTO,id);


        return "redirect:/users/qna/list";
    }
    //목록
    @GetMapping("/list")
    public String list(@PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,Principal principal,
                       Model model){
        if (principal == null) {
            // 로그인 안 된 경우 로그인 페이지로 보내기
            return "redirect:/users/login";
        }
        //로그인한 사장님의 이메일로 Qna리스트 가져오기
        String email = principal.getName();
        Page<QnaDTO> qnaDTOList = qnaService.qnaList(pageable, principal.getName());
        if (qnaDTOList.getPageable().isPaged()){
            log.info("현재 페이지 번호 : {} ",qnaDTOList.getPageable().getPageNumber());
        }else {
            log.info("페이징 정보 없음");
        }

        if (qnaDTOList.isEmpty()){
            model.addAttribute("message", "등록된 Qna가 없습니다.");
        }
        model.addAttribute("qnaDTOList",qnaDTOList);
        log.info(qnaDTOList.getContent());

        return "/users/qna/list";
    }



    //상세보기
    @GetMapping("/read")
    public String read(@RequestParam Long id, Model model, Principal principal, RedirectAttributes redirectAttributes){
        QnaDTO qnaDTO = qnaService.read(id);
        model.addAttribute("qnaDTO",qnaDTO);
        return "/users/qna/read";
    }
    //수정get
    @GetMapping("/update/{id}")
    public String update(@PathVariable(name = "id")Long id,Model model){
        log.info("수정" +id);
        QnaDTO qnaDTO = qnaService.read(id);
        model.addAttribute("qnaDTO",qnaDTO);
        return "/users/qna/update";
    }
    //수정post
    @PostMapping("/update")
    public String updatePost(QnaDTO qnaDTO){
        log.info(qnaDTO);
        qnaService.update(qnaDTO);
        return "redirect:/users/qna/read?id="+qnaDTO.getId();
    }
    //삭제
    @PostMapping("/delete")
    public String deleteGET(Long id){

        log.info("히히아이디" + id);
        qnaService.delete(id);
        return "redirect:/users/qna/list";
    }





}
