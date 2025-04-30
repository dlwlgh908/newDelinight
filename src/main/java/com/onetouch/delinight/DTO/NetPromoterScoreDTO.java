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


}
