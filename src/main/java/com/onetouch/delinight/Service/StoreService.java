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

    public Long findStoreByEmail(String email);
    public void create(StoreDTO storeDTO);

    public Integer awaitingCountCheck(Long storeId);

    public List<StoreDTO> list();

    public List<StoreDTO> list(String email);

    public void del(Long id);
    public StoreDTO read(Long id);

    public void update(StoreDTO storeDTO);



}
