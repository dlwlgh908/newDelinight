package com.onetouch.delinight.Util;

import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.DTO.PerformanceMailDTO;
import com.onetouch.delinight.DTO.StoreDTO;
import com.onetouch.delinight.Entity.CheckOutLogEntity;
import com.onetouch.delinight.Entity.UsersEntity;
import com.onetouch.delinight.Repository.CheckOutLogRepository;
import com.onetouch.delinight.Service.StoreService;
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
    private final StoreService storeService;

    // 매일 오전 9시에 실행 될 스케줄러
    @Scheduled(cron = "0 0 9 * * ?")
    public void sendNpsSurvey(){
        log.info("스케줄러 동작시작");
        // 어제 체크아웃한 고객들 찾기
        LocalDate yesterday = LocalDate.now().minusDays(1);
        log.info("어제 날짜 = {}", yesterday);
        List<CheckOutLogEntity> checkOutLogs = checkOutLogRepository.findByCheckoutDate(yesterday);
        log.info("체크아웃 유저 = {}",  checkOutLogs.size());

        if (checkOutLogs.isEmpty()) {
            log.info("어제 체크아웃한 고객이 없습니다.");
        }

            for (CheckOutLogEntity checkOutAddEmail : checkOutLogs) {
                UsersEntity usersEntity = checkOutAddEmail.getUsersEntity();
                if (usersEntity != null) {
                    String email = checkOutAddEmail.getUsersEntity().getEmail();
                    String name = checkOutAddEmail.getUsersEntity().getName();
                    Long checkOutId = checkOutAddEmail.getId();
                    log.info("체크아웃 ID: {}, 사용자 이름: {}, 이메일: {}", checkOutId, name, email);


                    String surveyLink = "http://localhost:8080/users/nps/survey/" + checkOutId;
                    log.info("설문 링크: {}", surveyLink);

                    emailService.sendNpsEmail(email, name, surveyLink, checkOutId);
                    log.info("NPS 설문 이메일 전송 완료: {}", email);
                }else{
                    log.info("CheckOut ID : {}에 연결된 사용자 정보가 없습니다.", checkOutAddEmail.getId());
                }

            }

    }

    @Scheduled(cron = "00 00 08 * * ?")
    public void sendDailyPerformance(){

        log.info("스케쥴러 작동 ");
        List<StoreDTO> storeDTOList = storeService.findAll();
        for(StoreDTO storeDTO : storeDTOList){
            if(storeDTO.getMembersDTO()!=null){
                MembersDTO membersDTO = storeDTO.getMembersDTO();
                PerformanceMailDTO mailDTO = new PerformanceMailDTO();
                mailDTO.setDate(LocalDate.now());
                mailDTO.setName(membersDTO.getName());
                mailDTO.setTargetName(storeDTO.getName());
                mailDTO.setEmail(membersDTO.getEmail());
                emailService.sendDailyPerformance(mailDTO);
            }
        }


    }

}

