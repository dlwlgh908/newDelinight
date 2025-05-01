package com.onetouch.delinight.Controller.Users;

import com.onetouch.delinight.DTO.NetPromoterScoreDTO;
import com.onetouch.delinight.Service.NetPromoterScoreService;
import com.onetouch.delinight.Util.NpsSurveyScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
        // NPS 설문 발송 서비스 호출
        try {
            netPromoterScoreService.sendNpsTemporary(checkOutId);
            log.info("NPS 설문 발송 완료, checkOutId: " + checkOutId);
            npsSurveyScheduler.sendNpsSurvey(); // NPS 설문 이메일 발송

            model.addAttribute("message", "NPS 설문 이메일이 실제 고객에게 발송되었습니다.");
        } catch (Exception e) {
            log.error("NPS 설문 발송 중 오류 발생", e);
            model.addAttribute("message", "NPS 설문 이메일 발송에 실패했습니다. 관리자에게 문의해주세요.");
        }

        return "users/nps/survey"; // 설문 페이지로 이동
    }

    @GetMapping("/survey/{checkOutId}")
    public String survey(@PathVariable Long checkOutId, Model model) {
        log.info("NPS 설문 데이터 조회, checkOutId : {} " + checkOutId);
        try {
            NetPromoterScoreDTO npsDTO = netPromoterScoreService.npsSelect(checkOutId);
            model.addAttribute("npsDTO", npsDTO);
            return "/users/nps/survey";
        }catch (Exception e) {
            log.info("NPS 설문 데이터 조회 실패", e);
            return "/redirect:/users/nps/survey";
        }
    }












}
