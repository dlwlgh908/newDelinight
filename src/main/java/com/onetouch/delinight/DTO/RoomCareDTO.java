/*********************************************************************
 * 클래스명 : RoomCareEntity
 * 기능 : 유저가 주문한 룸케어아이템들을 모아서 보낸다.
 * 작성자 : 박한나
 * 작성일 : 2025-04-25
 * 수정 : 2025-04-25
 *********************************************************************/
package com.onetouch.delinight.DTO;

import com.onetouch.delinight.Constant.RoomCareStatus;
import com.onetouch.delinight.Entity.CheckInEntity;
import com.onetouch.delinight.Entity.CheckOutLogEntity;
import com.onetouch.delinight.Entity.HotelEntity;
import com.onetouch.delinight.Entity.RoomCareItemEntity;
import com.onetouch.delinight.Entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomCareDTO{

    private Long id;

    private List<RoomCareItemEntity> roomCareItemEntities; // 룸케어아이템 리스트
    private HotelEntity hotelEntity; // 호텔
    private CheckInEntity checkInEntity; // 체크인 정보

    private LocalDateTime awaitingTime; // 요청시간
    private LocalDateTime deliveredTime; // 완료시간

    private CheckOutLogEntity checkOutLogEntity; // 체크아웃 정보

    RoomCareStatus roomCareStatus; // 요청완료 상태

}
