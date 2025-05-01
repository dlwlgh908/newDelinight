package com.onetouch.delinight.DTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NpsFormDataDTO {

    private Long checkOutId;

    // 호텔 설문 응답
    private List<Integer> hotelQuestions;

    // 스토어 설문 응답 (스토어 하나당 5개 문항씩, flat 구조로)
    private List<Integer> storeQuestions;

    private String etcQuestion;

    // 저장할 때 사용
    private boolean completed;

}
