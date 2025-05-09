/*********************************************************************
 * 클래스명 : RoomCareService
 * 기능 :
 * 작성자 : 박한나
 * 작성일 : 2025-04-25
 * 수정 : 2025-04-25
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.RoomCareItemDTO;
import com.onetouch.delinight.DTO.RoomCareRequestDTO;

import java.util.List;

public interface RoomCareService {

    public void orders(List<RoomCareItemDTO> roomCareItemDTOList, String email);
}
