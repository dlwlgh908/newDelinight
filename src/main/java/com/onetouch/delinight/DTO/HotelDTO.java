/*********************************************************************
 * 클래스명 : MembersDTO
 * 기능 :
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.DTO;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelDTO {

    private Long id;

    private String name;

    private String content;

    private BranchDTO branchDTO;
    private MembersDTO membersDTO;
    private Long imgNum;



    public HotelDTO setMembersDTO(MembersDTO membersDTO){
        this.membersDTO = membersDTO;
        return this;
    }

    public HotelDTO setImgNum(Long imgNum){
        this.imgNum = imgNum;
        return this;
    }



    public HotelDTO setBranchDTO(BranchDTO branchDTO){
        this.branchDTO = branchDTO;
        return this;
    }

}
