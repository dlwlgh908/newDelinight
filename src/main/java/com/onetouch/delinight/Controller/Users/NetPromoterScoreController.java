package com.onetouch.delinight.Controller.Users;

import com.onetouch.delinight.Service.NetPromoterScoreService;
import com.onetouch.delinight.Util.EmailService;
import com.onetouch.delinight.Util.NpsSurveyScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users/nps/")
@Log4j2
public class NetPromoterScoreController {

    private final NetPromoterScoreService netPromoterScoreService;
    private final EmailService emailService;
    private final NpsSurveyScheduler npsSurveyScheduler;

    @GetMapping("/npsmail")
    public String sendNpsTestMail(){
        npsSurveyScheduler.sendNpsSurvey();
        return null;
    }








}
