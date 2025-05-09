package com.onetouch.delinight.Controller.Users;


import com.onetouch.delinight.DTO.*;
import com.onetouch.delinight.Entity.UsersEntity;
import com.onetouch.delinight.Repository.UsersRepository;
import com.onetouch.delinight.Service.RoomCareMenuService;
import com.onetouch.delinight.Service.RoomCareService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users/roomCare")
@Log4j2
@Slf4j
public class UserRoomCareController {

    private final RoomCareService roomCareService;
    private final RoomCareMenuService roomCareMenuService;
    private final UsersRepository usersRepository;

    @GetMapping("/list")
    public String list(Principal principal){
        return "/users/roomCare/list";
    }

    @PostMapping("/rest/request")
    @ResponseBody
    public ResponseEntity<String> request(@RequestBody List<RoomCareItemDTO> requestDTO, Principal principal) {
        log.info(requestDTO);
        String email = principal.getName();
        roomCareService.orders(requestDTO, email);
        return null;
    }

    @GetMapping("/request")
    public String request(Principal principal, Model model){

        if (principal == null) {
            return "redirect:/users/login";
        }

        String email = principal.getName();
        log.info(email);

        // DB 사용자 이름 조회
        UsersEntity usersEntity = usersRepository.selectEmail(email);
        String name = usersEntity != null ? usersEntity.getName() : "고객";
        log.info(name+usersEntity);

        List<RoomCareMenuDTO> roomCareMenuDTOList = roomCareMenuService.list(email);
        HotelDTO hotelDTO = roomCareMenuDTOList.getFirst().getHotelDTO();

        model.addAttribute("menuDTOList",roomCareMenuDTOList);
        model.addAttribute("hotelDTO",hotelDTO);
        log.info(roomCareMenuDTOList);


        return "/users/roomCare/request";
    }
}
