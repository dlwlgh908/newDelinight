package com.onetouch.delinight.Controller;

import com.onetouch.delinight.Service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequestMapping("/cart")
@RequiredArgsConstructor
@RestController
@Log4j2
public class CartRestController {

private final CartService cartService;

    @PostMapping("/addToCart")
    public ResponseEntity<String> addToCart(Long menuNum, Principal principal){
        Long cartId = 1L; // 원래는 회원이면 principal로 해당 카트 찾아서 맵핑
        cartService.add(cartId, menuNum);
        return ResponseEntity.ok("저장 성공");

    }
    @PostMapping("/clear")
    public ResponseEntity<String> clearCart(Principal principal){
        Long cartId = 1L;
        cartService.clear(cartId);
        return ResponseEntity.ok("비우기 성공");
    }

}
