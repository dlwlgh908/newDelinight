package com.onetouch.delinight.Controller.Users;

import com.onetouch.delinight.DTO.NetPromoterScoreDTO;
import com.onetouch.delinight.Service.NetPromoterScoreService;
import com.onetouch.delinight.Util.NpsSurveyScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users/nps/")
@Log4j2
public class NetPromoterScoreController {

    private final NetPromoterScoreService netPromoterScoreService;
    private final NpsSurveyScheduler npsSurveyScheduler;

    @GetMapping("/npsmail")
    public String sendNpsTestMail(Model model) {
        log.info("NPS 메일 발송 테스트 시작");
        Long checkOutId = 14L;
        netPromoterScoreService.sendNpsTemporary(checkOutId);
        log.info("checkOutId: " + checkOutId);
        npsSurveyScheduler.sendNpsSurvey();
        model.addAttribute("message", "NPS 설문 이메일이 실제 고객에게 발송되었습니다.");
        return "users/nps/survey";
    }

    @GetMapping("/survey/{checkOutId}")
    public String survey(@PathVariable("checkOutId")Long checkOutId, Model model) {
        netPromoterScoreService.npsInsert(checkOutId);
        return "users/nps/survey";
    }








}
