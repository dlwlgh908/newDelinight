package com.onetouch.delinight.DTO;

import com.onetouch.delinight.Entity.NetPromoterScoreEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NetPromoterScoreDTO {

    private Long id;
    private List<CheckOutLogDTO> checkOutLogDTOS = new ArrayList<>();
    private StoreDTO storeDTO;
    private HotelDTO hotelDTO;

    // 설문 1 ~ 5 문항 리스트
    private List<Integer> hotelQuestions = new ArrayList<>(); // 호텔 설문
    private List<Integer> storeQuestions = new ArrayList<>(); // 스토어 설문
    // 기타 문의사항
    private String etcQuestion;

    // 호텔 + 스토어 토탈 스코어
    private int totalScore;

    // 호텔 || 호텔+스토어 이용한 경우
    public boolean isHotel() {
        return hotelDTO != null && storeDTO == null;
    }
    public boolean isHotelAndStore() {
        return hotelDTO != null && storeDTO != null;
    }

    // 호텔 설문을 엔티티에 매핑
    public void HotelSurveyTo(NetPromoterScoreEntity npsEntity) {
        if (hotelQuestions.size() >= 5) {
            npsEntity.setHotelQuestionOne(hotelQuestions.get(0));
            npsEntity.setHotelQuestionTwo(hotelQuestions.get(1));
            npsEntity.setHotelQuestionThree(hotelQuestions.get(2));
            npsEntity.setHotelQuestionFour(hotelQuestions.get(3));
            npsEntity.setHotelQuestionFive(hotelQuestions.get(4));
        }
        npsEntity.setEtcQuestion(this.etcQuestion);

        // 호텔 설문 점수 계산
        int hotelTotal = hotelQuestions.stream().mapToInt(Integer::intValue).sum();
        npsEntity.setHotelTotalScore((hotelTotal * 100) / 25);  // 호텔 설문은 25점 만점
    }

    // 스토어 설문을 엔티티에 매핑
    public void StoreSurveyTo(NetPromoterScoreEntity npsEntity) {
        if (storeQuestions.size() >= 5) {
            npsEntity.setStoreQuestionOne(storeQuestions.get(0));
            npsEntity.setStoreQuestionTwo(storeQuestions.get(1));
            npsEntity.setStoreQuestionThree(storeQuestions.get(2));
            npsEntity.setStoreQuestionFour(storeQuestions.get(3));
            npsEntity.setStoreQuestionFive(storeQuestions.get(4));
        }
        npsEntity.setEtcQuestion(this.etcQuestion);

        // 스토어 설문 점수 계산
        int storeTotal = storeQuestions.stream().mapToInt(Integer::intValue).sum();
        npsEntity.setTotalScore((storeTotal * 100) / 25);  // 스토어 설문은 25점 만점
    }

    // 총합 계산 메소드
    public void totalScore() {
        this.totalScore = 0;

        int hotelTotal = 0;
        int storeTotal = 0;

        // 호텔 설문
        if (hotelQuestions != null && !hotelQuestions.isEmpty()) {
            hotelTotal = hotelQuestions.stream().mapToInt(i -> i).sum();
        }

        // 스토어 설문
        if (storeQuestions != null && !storeQuestions.isEmpty()) {
            storeTotal = storeQuestions.stream().mapToInt(i -> i).sum();
        }

        // 호텔 설문만 있는 경우 || 호텔과 스토어 설문이 모두 있는 경우
        if (hotelQuestions != null && storeQuestions.isEmpty()) {
            this.totalScore = (hotelTotal * 100) / 25;  // 호텔 설문만 있을 때
        } else if (hotelQuestions != null && storeQuestions != null) {
            int total = hotelTotal + storeTotal;
            this.totalScore = (total * 100) / 50;  // 호텔과 스토어 설문 모두 있을 때
        }
    }

}
