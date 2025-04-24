package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.Repository.MembersRepository;
import com.onetouch.delinight.Service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hotel/rest")
@Log4j2
public class HotelRestController {
    private final HotelService hotelService;
    private final MembersRepository membersRepository;

    @DeleteMapping("/del")
    public void del(Long id){
        hotelService.del(id);
    }

    @PostMapping("/modify")
    public ResponseEntity<String> modify(@RequestParam Long id, @RequestParam Long hotelId) {

        log.info(id);
        log.info(hotelId);


        hotelService.modify(id, hotelId);


        return ResponseEntity.ok("성공");

    }
}
