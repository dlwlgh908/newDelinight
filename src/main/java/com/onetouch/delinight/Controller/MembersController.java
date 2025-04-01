/*********************************************************************
 * 클래스명 : MembersController
 * 기능 :
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.Controller;

import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.Service.MembersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
@Log4j2
public class MembersController {

    private final MembersService membersService;

    @GetMapping("/adminhome")
    public String adminhome() {
        return "members/home";

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

    @GetMapping("/list")
    public String list(Model model) {
        log.info("list페이지 진입");

        List<MembersDTO> membersDTOList =
            membersService.findAll();

        log.info(membersDTOList);
        model.addAttribute("memberlist", membersDTOList);



        return "members/list";
    }

    @GetMapping("/changestatus")
    public String changestatus(Long id) {

        log.info("changestatus 페이지 진입!!");
        log.info("changestatus 페이지 진입!!");
        log.info("changestatus 페이지 진입!!");
        log.info(id);
        log.info(id);
        log.info(id);
        log.info(id);

        return null;
    }
}
