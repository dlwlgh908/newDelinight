/*********************************************************************
 * 클래스명 : RoomCareItemController
 * 기능 :
 * 작성자 : 박한나
 * 작성일 : 2025-04-25
 * 수정 : 2025-04-25
 *********************************************************************/
package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.DTO.RoomCareItemDTO;
import com.onetouch.delinight.Service.RoomCareItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members/roomCare")
@Log4j2
public class RoomCareController {

    private final RoomCareItemService roomCareItemService;

    @GetMapping("/list")
    public String listView() {

        return "members/roomCare/list";
    }


}
