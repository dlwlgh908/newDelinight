/*********************************************************************
 * 클래스명 : MembersController
 * 기능 :
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.Controller.Users;

import com.onetouch.delinight.DTO.MenuDTO;
import com.onetouch.delinight.DTO.OrdersDTO;
import com.onetouch.delinight.Service.MenuService;
import com.onetouch.delinight.Service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/users/order/")

public class RoomServiceController {
    private final MenuService menuService;
    private final OrdersService ordersService;

    @GetMapping("/main")
    public String main(Model model){
        Long hotelNum = 1L;// 체크인 서비스에서 findHotelNum 메소드 만들어서 findbyCheckInNum로 룸찾고 룸에서 호텔 찾아서 넘겨줄 예정
        List<MenuDTO> menuDTOList = menuService.menuListByHotel(1L);
        model.addAttribute("menuDTOList", menuDTOList);
        log.info(menuDTOList);
        return "roomService/order/main";
    }

    @GetMapping("/list")
    public String list(Principal principal, Model model){
        List<OrdersDTO> ordersDTOList = ordersService.ordersListByEmail(principal.getName());
        model.addAttribute("ordersDTOList", ordersDTOList);
        return "roomService/order/list";
    }

    @GetMapping("/hub")
    public String hub(){
        return "redirect:/users/order/list";
    }

    @GetMapping("/request")
    public String request(Long paymentId, Model model){
        List<OrdersDTO> ordersDTOList = ordersService.read(paymentId);
        Long totalPrice = 0L;
     for(int i = 0; i<ordersDTOList.size(); i++){
         totalPrice += ordersDTOList.get(i).getTotalPrice();
        }

     log.info(totalPrice);
        log.info(ordersDTOList);
     model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("ordersDTOList", ordersDTOList);
        return "roomService/order/request";
    }




}
