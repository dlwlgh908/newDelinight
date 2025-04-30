/*********************************************************************
 * 클래스명 : RoomCareItemService
 * 기능 :
 * 작성자 : 박한나
 * 작성일 : 2025-04-25
 * 수정 : 2025-04-25
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.RoomCareItemDTO;
import java.util.List;

public interface RoomCareItemService {

    //등록
    public void create(RoomCareItemDTO roomCareItemDTO, String email);
    //수정
    public void update(RoomCareItemDTO roomCareItemDTO, String email);
    //목록
    public List<RoomCareItemDTO> list();
    //삭제
    public void del(Long roomCareItemId, String email);
}
