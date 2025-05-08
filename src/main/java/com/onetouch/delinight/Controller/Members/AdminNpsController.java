package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.Service.NetPromoterScoreService;
import com.onetouch.delinight.Util.MemberDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members/nps/")
@Log4j2
public class AdminNpsController {

    private final NetPromoterScoreService netPromoterScoreService;



    @GetMapping("/list")
    public String list(@AuthenticationPrincipal MemberDetails memberDetails, Model model) {
        Long memberId = memberDetails.getMembersEntity().getId();
        model.addAttribute("npsList", netPromoterScoreService.findAll(memberId));
        return "members/nps/list";
    }

    @GetMapping("/chart")
    public String chart(@AuthenticationPrincipal MemberDetails memberDetails, Model model) {
        Long memberId = memberDetails.getMembersEntity().getId();
        model.addAttribute("npsList", netPromoterScoreService.findAll(memberId));
        return "members/nps/chart";
    }






}
