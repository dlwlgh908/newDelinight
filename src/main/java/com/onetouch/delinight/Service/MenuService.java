/*********************************************************************
 * 클래스명 : MembersService
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-04-01
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.MenuDTO;

import java.util.List;

public interface MenuService {

    public List<MenuDTO> menuListByHotel(Long hotelNum);


}
