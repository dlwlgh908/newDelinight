/*********************************************************************
 * 클래스명 : MembersService
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.BranchDTO;
import com.onetouch.delinight.DTO.HotelDTO;

import java.util.List;

public interface HotelService {

    public void create(HotelDTO hotelDTO);

    public List<HotelDTO> list();

    public void del(Long id);



}
