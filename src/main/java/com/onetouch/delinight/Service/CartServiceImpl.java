/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.Menu;
import com.onetouch.delinight.Entity.CartEntity;
import com.onetouch.delinight.Entity.MenuEntity;
import com.onetouch.delinight.Repository.CartRepository;
import com.onetouch.delinight.Repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{
    private final CartRepository cartRepository;
    private final MenuRepository menuRepository;

    @Override
    public String clear(Long cartNum) {
        CartEntity cartEntity = cartRepository.findById(cartNum).get();
        cartEntity.setMenuEntityList(null);
        cartEntity.setTotalPrice(null);
        return "카트 비우기 성공";
    }

    @Override
    public String add(Long cartNum, Long menuNum) {
       MenuEntity selectedMenu = menuRepository.findById(menuNum).get();
        CartEntity cartEntity = cartRepository.findById(cartNum).get();
        List<MenuEntity> cartMenuEntityList = cartEntity.getMenuEntityList();
        cartMenuEntityList.add(selectedMenu);
        cartEntity.setMenuEntityList(cartMenuEntityList);
        log.info(cartRepository.findById(1L).get());
        return "카트 메뉴 추가 성공";
    }
}
