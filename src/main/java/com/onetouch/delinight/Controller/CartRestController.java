package com.onetouch.delinight.Controller;

import com.onetouch.delinight.DTO.CartItemDTO;
import com.onetouch.delinight.Service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RequestMapping("/cart")
@RequiredArgsConstructor
@RestController
@Log4j2
public class CartRestController {

    private final CartService cartService;

    @GetMapping("/showList")
    public ResponseEntity<List<CartItemDTO>> showList(Principal principal) {
        Long cartNum = 1L;
        List<CartItemDTO> cartItemDTOList = cartService.list(cartNum);
        return ResponseEntity.ok(cartItemDTOList);
    }

    @PostMapping("/addToCart")
    public ResponseEntity<String> addToCart(Long menuNum, Principal principal) {
        Long cartId = 1L; // 원래는 회원이면 principal로 해당 카트 찾아서 맵핑
        Integer existCartItem = cartService.add(cartId, menuNum);
        if (existCartItem == 2) {
            return ResponseEntity.ok("이미 장바구니에 담긴 음식입니다. 장바구니에서 수량을 조정해주세요.");
        } else {
            return ResponseEntity.ok("저장 성공");
        }

    }

    @PostMapping("/removeFromCart")
    public ResponseEntity<String> removeFromCart(Long cartItemNum, Principal principal) {

        cartService.remove(cartItemNum);

        return ResponseEntity.ok("삭제 성공");


    }

    @PostMapping("/clear")
    public ResponseEntity<String> clearCart(Principal principal) {
        Long cartId = 1L;
        cartService.clear(cartId);
        return ResponseEntity.ok("비우기 성공");
    }


    @PostMapping("/cartToOrder")
    public String  cartToOrder(Principal principal) {
        Long cartId = 1L;
        Long paymentId = cartService.cartToOrder(cartId);
        return "redirect:/roomService/order/read?paymentId="+paymentId;
    }

    @PostMapping("/plusQuantity")
    private ResponseEntity<String> plusQuantity(Long cartItemNum){
        String result = cartService.plusQuantity(cartItemNum);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/minusQuantity")
    private ResponseEntity<String> minusQuantity(Long cartItemNum){
        String result = cartService.minusQuantity(cartItemNum);
        return ResponseEntity.ok(result);
    }


}
