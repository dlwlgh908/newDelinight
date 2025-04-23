/*********************************************************************
 * 클래스명 : MembersDTO
 * 기능 :
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.DTO;

import com.onetouch.delinight.Entity.CheckInEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class InquireDTO {

    private Long id;
    @NotBlank(message = "제목을 작성해주세요.")
    @Size(min = 3,max = 50,message = "3~50글자로 작성해주세요.")
    private String title;
    @NotBlank(message = "내용을 작성해주세요.")
    @Size(min = 2, max = 2000, message = "2~2000글자로 작성헤주세요.")
    private String content;
    private LocalDateTime regtime; //등록시간
    private LocalDateTime updatetime; //답변시간(response time)



    private CheckInEntity checkInEntity;

    private CheckInDTO checkInDTO;
    private HotelDTO hotelDTO;
    private UsersDTO usersDTO;

    public InquireDTO setcheckInDTO(CheckInDTO checkInDTO){
        this.checkInDTO =checkInDTO;
        return this;
    }

    public InquireDTO sethotelDTO(HotelDTO hotelDTO){
        this.hotelDTO = hotelDTO;
        return this;
    }



}
