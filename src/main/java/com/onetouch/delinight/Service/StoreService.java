/*********************************************************************
 * 클래스명 : MembersService
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.HotelDTO;
import com.onetouch.delinight.DTO.StoreDTO;

import java.util.List;

public interface StoreService {

    public void create(StoreDTO storeDTO);


    public List<StoreDTO> list();

    public void del(Long id);


}
