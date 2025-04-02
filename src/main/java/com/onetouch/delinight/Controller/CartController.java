/*********************************************************************
 * 클래스명 : CartController
 * 기능 :
 * 작성자 : 이지호
 * 작성일 : 2025-03-30
 * 수정 : 2025-04-01     이지호
 *********************************************************************/
package com.onetouch.delinight.Controller;

import com.onetouch.delinight.DTO.CheckInDTO;
import com.onetouch.delinight.DTO.MenuDTO;
import com.onetouch.delinight.DTO.RoomDTO;
import com.onetouch.delinight.Service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
@Log4j2
public class CartController {
    private final MenuService menuService;

    @GetMapping("/list")
    public String list(Model model){
        Long hotelNum = 1L;// 체크인 서비스에서 findHotelNum 메소드 만들어서 findbyCheckInNum로 룸찾고 룸에서 호텔 찾아서 넘겨줄 예정
        List<MenuDTO> menuDTOList = menuService.menuListByHotel(1L);
        model.addAttribute("menuDTOList", menuDTOList);
        log.info(menuDTOList);
        return "roomService/cart/list";
    }



}
