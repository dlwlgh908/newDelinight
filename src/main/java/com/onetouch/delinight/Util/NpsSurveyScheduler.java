package com.onetouch.delinight.Util;

import com.onetouch.delinight.Entity.CheckOutLogEntity;
import com.onetouch.delinight.Entity.UsersEntity;
import com.onetouch.delinight.Repository.CheckOutLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class NpsSurveyScheduler {

    private final CheckOutLogRepository checkOutLogRepository;
    private final EmailService emailService;

    // 매일 오전 9시에 실행 될 스케줄러
    @Scheduled(cron = "0 0 9 * * ?")
    public void sendNpsSurvey(){
        // 어제 체크아웃한 고객들 찾기
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<CheckOutLogEntity> checkOutLogs = checkOutLogRepository.findByCheckoutDate(yesterday);

            for (CheckOutLogEntity checkOutAddEmail : checkOutLogs) {
                UsersEntity usersEntity = checkOutAddEmail.getUsersEntity();
                if (usersEntity != null) {
                    String email = checkOutAddEmail.getUsersEntity().getEmail();
                    String name = checkOutAddEmail.getUsersEntity().getName();
                    Long checkOutId = checkOutAddEmail.getId();

                    String surveyLink = "http://localhost:8080/users/nps/survey/" + checkOutId;
                    emailService.sendNpsEmail(email, name, surveyLink, checkOutId);
                }else{
                    log.info("CheckOut ID : {}에 연결된 사용자 정보가 없습니다.", checkOutAddEmail.getId());
                }

            }

    }

}

