/*********************************************************************
 * 클래스명 : MembersService
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.CartItemDTO;
import com.onetouch.delinight.Entity.GuestEntity;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

public interface CartService {

    public Long cartCheck(String email);

    public List<CartItemDTO> list(Long cartNum);
    public void makeCart(int sep, Long id);
    public void deleteCart(int sep, Long cartNum);

    public Integer add(Long cartNum, Long menuNum);
    public String clear(Long cartNum);

    public void remove(Long cartItemNum);

    public Long cartToOrder(Long cartNum);
    public String plusQuantity(Long cartItemNum);
    public String minusQuantity(Long cartItemNum);


}
