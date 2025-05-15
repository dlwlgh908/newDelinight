/*********************************************************************
 * 클래스명 : MembersService
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.HotelDTO;
import com.onetouch.delinight.DTO.MembersDTO;

import java.util.List;

public interface HotelService {

    public int assignCheck(String email);
    public Integer unansweredCheck(Long id);
    public void addMembers(Long memberId, Long hotelId);
    public void create(HotelDTO hotelDTO);

    public void update(HotelDTO hotelDTO);

    public List<HotelDTO> list(MembersDTO membersDTO);

    public HotelDTO findHotelDTOById(Long id);
    public Long findHotelByEmail(String email);



    public void del(Long id);

    public void modify(Long memberId, Long hotelId);






}
