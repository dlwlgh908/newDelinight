package com.onetouch.delinight.Entity;

import com.onetouch.delinight.Entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class NetPromoterScoreEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "netpromoterscore_id")
    private Long id;

    @OneToMany(mappedBy = "netPromoterScoreEntity", fetch = FetchType.LAZY)
    private List<CheckOutLogEntity> checkOutLogEntities = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private StoreEntity storeEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private HotelEntity hotelEntity;

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

    // 기타 문의사항
    private String etcQuestion;

    // 호텔 설문만의 총점
    @Column(name = "hotel_total_score")
    private int hotelTotalScore;

    // 호텔 + 스토어 설문의 총점
    @Column(name = "total_score")
    private int totalScore;




}
