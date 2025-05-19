/*********************************************************************
 * 클래스명 : MembersService
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.RoomDTO;

import java.util.List;

public interface RoomService {

    public void create(RoomDTO roomDTO, String email);

    public void update(RoomDTO roomDTO);

    public List<RoomDTO> list(String email);

    public void del(Long id);





}
