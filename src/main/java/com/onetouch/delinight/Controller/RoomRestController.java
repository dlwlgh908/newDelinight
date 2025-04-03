package com.onetouch.delinight.Controller;

import com.onetouch.delinight.DTO.RoomDTO;
import com.onetouch.delinight.Service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/room/rest")
@Log4j2
public class RoomRestController {
    private final RoomService roomService;

    @PostMapping("/register")
    private ResponseEntity register(RoomDTO roomDTO) {
        log.info("값이 들어오나요? : " + roomDTO );
        log.info("값이 들어오나요? : " + roomDTO );
        log.info("값이 들어오나요? : " + roomDTO );


        if (roomDTO == null) {
            return new ResponseEntity<String>("저장 실패", HttpStatus.BAD_REQUEST);
        } else {
            roomDTO = roomService.create(roomDTO);
            log.info(roomDTO.getId());
            log.info(roomDTO.getId());
            log.info(roomDTO.getId());

            return new ResponseEntity<String>(roomDTO.getId() + "번글이 저장되었습니다.", HttpStatus.OK);

        }



    }
}
