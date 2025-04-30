/*********************************************************************
 * 클래스명 : RoomCareItemEntity
 * 기능 : 룸케어 메뉴 항목들
 * 작성자 : 박한나
 * 작성일 : 2025-04-25
 * 수정 : 2025-04-25
 *********************************************************************/
package com.onetouch.delinight.DTO;

import com.onetouch.delinight.Entity.RoomCareEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomCareItemDTO {

    private Long id; //룸케어아이템 코드번호
    private String name; //룸케어아이템명
    private String content; //룸케어아이템 내용

    private RoomCareEntity roomCareEntity; // 룸케어
    private Long hotelId; // 호텔
}
