package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.DTO.InquireDTO;
import com.onetouch.delinight.Repository.InquireRepository;
import com.onetouch.delinight.Service.InquireService;
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
@RequestMapping("/members/inquire")
public class MembersInquireController {
    private final InquireService inquireService;
    private final InquireRepository inquireRepository;



    // 멤버에서는 읽기(읽기 페이지 속에 댓글이 들어가는거임), 리스트


    //목록
    @GetMapping("/list")
    public String list(@PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,Principal principal,
                       Model model){
        if (principal == null) {
            // 로그인 안 된 경우 로그인 페이지로 보내기
            return "redirect:/users/login";
        }
        //로그인한 사장님의 이메일로 Inquire리스트 가져오기
        String email = principal.getName();
        Page<InquireDTO> inquireDTOList = inquireService.inquireList(pageable, principal.getName());
        if (inquireDTOList.getPageable().isPaged()){
            log.info("현재 페이지 번호 : {} ",inquireDTOList.getPageable().getPageNumber());
        }else {
            log.info("페이징 정보 없음");
        }

        if (inquireDTOList.isEmpty()){
            model.addAttribute("message", "등록된 Inquire가 없습니다.");
        }
        model.addAttribute("inquireDTOList",inquireDTOList);
        log.info(inquireDTOList.getContent());

        return "/members/inquire/list";
    }



    //상세보기
    @GetMapping("/read")
    public String read(@RequestParam Long id, Model model, Principal principal, RedirectAttributes redirectAttributes){
        InquireDTO inquireDTO = inquireService.read(id);
        model.addAttribute("inquireDTO", inquireDTO);
        return "/members/inquire/read";
    }



}
