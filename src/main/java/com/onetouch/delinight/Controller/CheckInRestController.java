package com.onetouch.delinight.Controller;

import com.onetouch.delinight.DTO.CheckInDTO;
import com.onetouch.delinight.DTO.GuestDTO;
import com.onetouch.delinight.Service.CheckInService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/checkin/rest")
@Log4j2
public class CheckInRestController {
    private final CheckInService checkInService;

    @PostMapping("/register")
    private ResponseEntity guestRegister(@RequestBody CheckInDTO chekCheckInDTO, @RequestBody GuestDTO guestDTO) {

        log.info("들어오는 값 : "+chekCheckInDTO);
        log.info("들어오는 값 : "+chekCheckInDTO);
        log.info("들어오는 값 : "+chekCheckInDTO);
        log.info("들어오는 값 : "+chekCheckInDTO);

        log.info(guestDTO.getPhone());
        log.info(guestDTO.getPhone());
        log.info(guestDTO.getPhone());
        log.info(guestDTO.getPhone());


        return null;
    }


}
