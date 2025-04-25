/*********************************************************************
 * 클래스명 : MembersController
 * 기능 :
 * 작성자 : 이효찬
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이효찬
 *********************************************************************/
package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.DTO.RoomDTO;
import com.onetouch.delinight.Service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members/room")
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/create")
    public String createView() {

        return "members/room/create";
    }

    @PostMapping("/create")
    public String createProc(RoomDTO roomDTO) {

        roomService.create(roomDTO);
        return "redirect:/members/room/list";
    }

    @GetMapping("/list")
    public String listView(Model model) {
        List<RoomDTO> roomDTOList =
            roomService.list();
        model.addAttribute("roomDTOList", roomDTOList);
        return "members/room/listB";
    }
}
