/*********************************************************************
 * 클래스명 : MembersService
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.CartItemDTO;

import java.util.List;

public interface CartService {

    public List<CartItemDTO> list(Long cartNum);
    public Integer add(Long cartNum, Long menuNum);
    public String clear(Long cartNum);

    public void remove(Long cartItemNum);

    public String cartToOrder(Long cartNum);
    public String plusQuantity(Long cartItemNum);
    public String minusQuantity(Long cartItemNum);


}
