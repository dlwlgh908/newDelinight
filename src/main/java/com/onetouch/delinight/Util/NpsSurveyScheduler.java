package com.onetouch.delinight.Util;

import com.onetouch.delinight.Repository.CheckOutLogRepository;
import com.onetouch.delinight.Repository.NetPromoterScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class NpsSurveyScheduler {

    private final CheckOutLogRepository checkOutLogRepository;
    private final NetPromoterScoreRepository netPromoterScoreRepository;
    private final EmailService emailService;

    // 매일 오전 9시에 실행 될 스케줄러
    @Scheduled(cron = "0 0 9 * * ?")
    public void sendNpsSurvey() {
        // 어제 체크아웃한 고객들 찾기
        LocalDate yesterday = LocalDate.now().minusDays(1);

    }



}
