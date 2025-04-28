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

    public Integer unansweredCheck(Long id);
    public void addMembers(Long memberId, Long hotelId);
    public void create(HotelDTO hotelDTO, String email);

    public void update(HotelDTO hotelDTO);

    public List<HotelDTO> list();

    public Long findHotelByEmail(String email);



    public void del(Long id);

    public void modify(Long id, Long hotelId);




}
