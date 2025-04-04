/*********************************************************************
 * 클래스명 : MembersController
 * 기능 :
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.Controller;

import com.onetouch.delinight.DTO.CheckInDTO;
import com.onetouch.delinight.DTO.RoomDTO;
import com.onetouch.delinight.Service.CheckInService;
import com.onetouch.delinight.Service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/checkin")
@Log4j2
public class CheckInController {

    private final CheckInService checkInService;
    private final RoomService roomService;

    @GetMapping("/list")
    public String listView(Model model) {

        List<RoomDTO> roomDTOList =
            roomService.list();

        List<CheckInDTO> checkInDTOList =
                checkInService.list();

        log.info(checkInDTOList);
        log.info(checkInDTOList);


        model.addAttribute("roomDTOList", roomDTOList);
        model.addAttribute("checkInDTOList", checkInDTOList);




        return "checkin/list";

    }
}
