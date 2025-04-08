package com.onetouch.delinight.Controller;

import com.onetouch.delinight.DTO.CheckInDTO;
import com.onetouch.delinight.DTO.GuestDTO;
import com.onetouch.delinight.Service.CheckInService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/checkin/rest")
@Log4j2
public class CheckInRestController {
    private final CheckInService checkInService;

    @PostMapping("/register")
    public ResponseEntity<String> checkin(@RequestBody CheckInDTO checkInDTO) {


        log.info("들어오는 값 : " + checkInDTO);
        log.info("들어오는 값 : " + checkInDTO);
        log.info("들어오는 값 : " + checkInDTO);
        log.info("들어오는 값 : " + checkInDTO);

        log.info(checkInDTO.getId());
        log.info(checkInDTO.getId());
        log.info(checkInDTO.getId());
        log.info(checkInDTO.getId());

        checkInService.checkin(checkInDTO);


        return ResponseEntity.ok("성공");
    }

    @PostMapping("/out")
    public ResponseEntity<String> checkout(@RequestParam Long id) {
        log.info("받은 id : " + id);
        log.info("받은 id : " + id);
        log.info("받은 id : " + id);

        checkInService.checkout(id);

        return ResponseEntity.ok("성공");

    }

    @PostMapping("/reservation")
    public ResponseEntity<Map<String, Object>> reservation(String reservNum) {
        log.info("받은 값 : " + reservNum);
        log.info("받은 값 : " + reservNum);
        log.info("받은 값 : " + reservNum);

        String[] parts = reservNum.split("/");

        Long roomid = Long.parseLong(parts[0]);
        String checkinDate = parts[1].substring(0, 6);
        String checkoutDate = parts[1].substring(6, 12);
        int certid = Integer.parseInt(parts[2]);

        log.info(certid);

        Map<String, Object> response = new HashMap<>();
        response.put("roomid", roomid);
        response.put("checkinDate", checkinDate);
        response.put("checkoutDate", checkoutDate);
        response.put("certid", certid);



        return ResponseEntity.ok(response);
    }

}
