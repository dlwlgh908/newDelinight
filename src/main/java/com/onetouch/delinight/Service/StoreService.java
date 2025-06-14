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
import com.onetouch.delinight.DTO.StoreDTO;

import java.util.List;

public interface StoreService {

    public void addMembers(Long memberId, Long storeId) throws Exception;

    public void modiMembers(Long memberId, Long storeId);

    public int assignCheck(String email);
    public Long findStoreByEmail(String email);
    public void create(StoreDTO storeDTO);

    public Integer awaitingCountCheck(Long storeId);

    public List<StoreDTO> listMembers(MembersDTO membersDTO);

    public List<StoreDTO> list(String email);

    public void del(Long id);
    public StoreDTO read(Long id);

    public void update(StoreDTO storeDTO);

    public List<StoreDTO> storeList(String email);

    public List<StoreDTO> findOperationStore();




}
