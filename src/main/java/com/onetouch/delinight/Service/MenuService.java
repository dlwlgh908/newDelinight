/*********************************************************************
 * 클래스명 : MembersService
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.MenuDTO;
import com.onetouch.delinight.Entity.MenuEntity;
import com.onetouch.delinight.Repository.MenuRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface MenuService {

    //등록
    public void register(MenuDTO menuDTO);
    //상세보기
    public MenuDTO read(Long id);
    //목록
    public Page<MenuDTO> menuList(Pageable pageable);
    //수정
    public MenuDTO update(MenuDTO menuDTO);
    //삭제
    public void delete(Long id);





}
