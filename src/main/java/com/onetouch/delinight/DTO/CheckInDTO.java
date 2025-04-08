/*********************************************************************
 * 클래스명 : MembersDTO
 * 기능 :
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.DTO;

import com.onetouch.delinight.Constant.CheckInStatus;
import com.onetouch.delinight.Entity.CheckInEntity;
import com.onetouch.delinight.Entity.GuestEntity;
import com.onetouch.delinight.Entity.RoomEntity;
import com.onetouch.delinight.Entity.UsersEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInDTO {

    private Long id;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkinDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkoutDate;

    private int price;


    private UsersDTO usersDTO;

    private GuestDTO guestDTO;

    private RoomDTO roomDTO;

    private CheckInStatus checkInStatus;

    public CheckInDTO setRoomDTO(RoomDTO roomDTO) {
        this.roomDTO = roomDTO;
        return this;
    }


    public CheckInDTO setGuestDTO(GuestDTO guestDTO) {
        this.guestDTO = guestDTO;
        return this;
    }




}
