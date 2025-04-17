/*********************************************************************
 * 클래스명 : MembersController
 * 기능 :
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.Controller;

import com.onetouch.delinight.DTO.CenterDTO;
import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.Service.CenterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members/center")
@Log4j2
public class CenterController {

    private final CenterService centerService;

    @GetMapping("/create")
    public String createView() {


        return "center/create";
    }

    @PostMapping("/create")
    public String createProc(CenterDTO centerDTO, Principal principal) {
        log.info("center create 진입");
        log.info("center create 진입");
        log.info("center create 진입");

        String email =
                principal.getName();

        MembersDTO membersDTO =
                centerService.checkEmail(email);

        log.info(membersDTO);
        log.info(membersDTO);
        log.info(membersDTO);

        centerService.create(centerDTO, email);

        return "redirect:/members/center/list";

    }

    @GetMapping("/list")
    public String listView(Model model) {
        List<CenterDTO> centerDTOList =
                centerService.list();
        model.addAttribute("centerDTOList", centerDTOList);
        return "center/list";
    }

    @GetMapping("/read")
    public String readView(Principal principal, Model model) {
        log.info("principal log ! : " + principal.getName());
        log.info("principal log ! : " + principal.getName());
        log.info("principal log ! : " + principal.getName());

        CenterDTO centerDTO =
                centerService.read(principal.getName());
        model.addAttribute("centerDTO", centerDTO);


        return "center/read";

    }
}
