package com.onetouch.delinight.DTO;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NetPromoterScoreDTO {

    private Long id;
    private Long checkOutId;
    private int QuestionOne;
    private int QuestionTwo;
    private int QuestionThree;
    private int QuestionFour;
    private int QuestionFive;
    private String hotelOrStore;
    private Long hotelId;
    private Long storeId;

}
