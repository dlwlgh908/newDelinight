package com.onetouch.delinight.Controller.Users;

import com.onetouch.delinight.DTO.NetPromoterScoreDTO;
import com.onetouch.delinight.Service.NetPromoterScoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users/nps/")
@Log4j2
public class NetPromoterScoreController {

    private final NetPromoterScoreService netPromoterScoreService;


    @GetMapping("/survey")
    public String survey(){
        return "users/nps/survey";
    }

    @PostMapping("/survey")
    public String survey(@ModelAttribute NetPromoterScoreDTO netPromoterScoreDTO) {
        log.info("NPS 설문 제출 : {}", netPromoterScoreDTO);

        // 1. totalScore 계산
        netPromoterScoreDTO.totalScore();
        // 2. 저장
        netPromoterScoreService.npsInsert(netPromoterScoreDTO);
        // 3. 결과 페이지로 이동
        return "redirect:/users/nps/survey";

    }


}
