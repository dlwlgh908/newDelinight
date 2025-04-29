package com.onetouch.delinight.DTO;

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

    // 호텔 설문 1 ~ 5 문항
    private int hotelQuestionOne;
    private int hotelQuestionTwo;
    private int hotelQuestionThree;
    private int hotelQuestionFour;
    private int hotelQuestionFive;
    // 스토어 설문 1 ~ 5 문항
    private int storeQuestionOne;
    private int storeQuestionTwo;
    private int storeQuestionThree;
    private int storeQuestionFour;
    private int storeQuestionFive;
    // 호텔 + 스토어 토탈 스코어
    private int totalScore;

    // 1 ~ 5 입력값을 받아서 10점 단위로 변환하고, totalScore 계산하는 메소드
    public void totalScore() {
        // 1. 입력값을 10점 단위로 환산
        this.hotelQuestionOne *= 10;
        this.hotelQuestionTwo *= 10;
        this.hotelQuestionThree *= 10;
        this.hotelQuestionFour *= 10;
        this.hotelQuestionFive *= 10;
        this.storeQuestionOne *= 10;
        this.storeQuestionTwo *= 10;
        this.storeQuestionThree *= 10;
        this.storeQuestionFour *= 10;
        this.storeQuestionFive *= 10;
        // 2. 총합 계산
        this.totalScore =
                hotelQuestionOne
              + hotelQuestionTwo
              + hotelQuestionThree
              + hotelQuestionFour
              + hotelQuestionFive
              + storeQuestionOne
              + storeQuestionTwo
              + storeQuestionThree
              + storeQuestionFour
              + storeQuestionFive;
    }

}
