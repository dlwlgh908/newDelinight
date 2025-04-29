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
    @Column(name = "hqt_one")
    private int hotelQuestionOne;
    @Column(name = "hqt_two")
    private int hotelQuestionTwo;
    @Column(name = "hqt_three")
    private int hotelQuestionThree;
    @Column(name = "hqt_four")
    private int hotelQuestionFour;
    @Column(name = "hqt_five")
    private int hotelQuestionFive;
    // 스토어 설문 1 ~ 5 문항
    @Column(name = "sqt_one")
    private int storeQuestionOne;
    @Column(name = "sqt_two")
    private int storeQuestionTwo;
    @Column(name = "sqt_three")
    private int storeQuestionThree;
    @Column(name = "sqt_four")
    private int storeQuestionFour;
    @Column(name = "sqt_five")
    private int storeQuestionFive;
    // 호텔 + 스토어 토탈 스코어
    @Column(name = "total_score")
    private int totalScore;




}
