/*********************************************************************
 * 클래스명 : MembersController
 * 기능 :
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.Controller;

import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Repository.MembersRepository;
import com.onetouch.delinight.Service.MembersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members/account")
@Log4j2
public class MembersController {

    private final MembersService membersService;
    private final MembersRepository membersRepository;

    @ModelAttribute("membersDTO")
    public MembersDTO setUserInfo(Principal principal) {
        if (principal == null) return null;

        String email = principal.getName();
        MembersEntity membersEntity = membersRepository.findByEmail(email);
        return new MembersDTO(membersEntity);
    }

    @GetMapping("/adminhome")
    public String adminhome() {
        return "/members/adminhome";

    }

    @GetMapping("/create")
    public String createView() {
        return "members/create";
    }

    @PostMapping("/create")
    public String createProc(MembersDTO membersDTO) {
        membersService.create(membersDTO);

        return "redirect:members/adminhome";
    }

    @GetMapping("/hoteladmincreate")
    public String hoteladmincreateView() {
        return "members/hoteladmincreate";
    }
    @PostMapping("/hoteladmincreate")
    public String hoteladmincreate(MembersDTO membersDTO) {
        membersService.hoteladcreate(membersDTO);

        return "redirect:/members/adminhome";
    }

    @GetMapping("/storeadmincreate")
    public String storeadmincreateView() {
        return "members/storeadmincreate";
    }
    @PostMapping("/storeadmincreate")
    public String storeadmincreate(MembersDTO membersDTO) {
        membersService.storeadcreate(membersDTO);

        return "redirect:/members/adminhome";
    }



    @GetMapping("/list")
    public String list(Model model) {
        log.info("list페이지 진입");

        List<MembersDTO> membersDTOList =
            membersService.findSuper();

        log.info(membersDTOList);
        model.addAttribute("memberlist", membersDTOList);

        return "members/list";
    }

    @GetMapping("/hoteladlist")
    public String hoteladlist(Model model) {
        log.info("list페이지 진입");

        List<MembersDTO> membersDTOList =
                membersService.findHotelAd();

        log.info(membersDTOList);
        model.addAttribute("memberlist", membersDTOList);

        return "members/list";
    }

    @GetMapping("/storeadlist")
    public String storeadlist(Model model) {
        log.info("list페이지 진입");

        List<MembersDTO> membersDTOList =
                membersService.findStoreAd();

        log.info(membersDTOList);
        model.addAttribute("memberlist", membersDTOList);

        return "members/list";
    }

    @GetMapping("/adminlogin")
    public String adminlogin(@RequestParam(value = "error", required = false) String error, Model model) {

        if ("bad_credentials".equals(error)) {
            model.addAttribute("passwordError", "비밀번호가 틀립니다.");
        } else if ("unknown".equals(error)) {
            model.addAttribute("emailError", "이메일이 틀립니다.");
        }

        return "/members/adminlogin";
    }

    @GetMapping("/adminlogout-success")
    public String adminlogout(){
        return "/members/adminlogout";
    }
}
