/*********************************************************************
 * 클래스명 : MembersController
 * 기능 :
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.Controller;

import com.onetouch.delinight.DTO.StoreDTO;
import com.onetouch.delinight.Service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/store/orders")
public class OrderController {
    private final OrdersService ordersService;

    @GetMapping("/list")
    public String orderList(Principal principal, Model model){
        String email = "hansin@a.a";
        StoreDTO storeDTO = ordersService.findStoreByADMINEmail(email);
        model.addAttribute("storeDTO", storeDTO);
        return "orders/list";

    }

}
