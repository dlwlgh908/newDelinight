/*********************************************************************
 * 클래스명 : MembersController
 * 기능 :
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.Controller;

import com.onetouch.delinight.DTO.HotelDTO;
import com.onetouch.delinight.Service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/hotel")
public class HotelController {


    private final HotelService hotelService;

    @GetMapping("/create")
    public String createView() {


        return "hotel/create";
    }

    @PostMapping("/create")
    public String createProc(HotelDTO hotelDTO) {
        hotelService.create(hotelDTO);

        return "hotel/create";
    }

    @GetMapping("/list")
    public String listView(Model model) {
        List<HotelDTO> hotelDTOList=
        hotelService.list();

        model.addAttribute("hotelDTOList", hotelDTOList);
        return "hotel/list";
    }
}
