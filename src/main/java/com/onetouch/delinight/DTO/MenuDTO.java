/*********************************************************************
 * 클래스명 : MenuDTO
 * 기능 : 메뉴 시스템 구현
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.DTO;



import com.onetouch.delinight.Constant.MenuStatus;
import com.onetouch.delinight.Config.MenuStatus;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuDTO {


    private Long id; //메뉴 코드번호

    private String name; //메뉴명

    private String content; //내용

    private int price; //가격

    MenuStatus menuStatus; //판매상태

    private String createBy;
    private String status;


    private StoreDTO storeDTO;

    private Long imgNum;

    private String imgUrl;

    public MenuDTO setStoreDTO(StoreDTO storeDTO){
        this.storeDTO = storeDTO;
        return this;
    }


    public MenuDTO setStoreDTO(StoreDTO storeDTO){
        this.storeDTO = storeDTO;
        return this;
    }

}
