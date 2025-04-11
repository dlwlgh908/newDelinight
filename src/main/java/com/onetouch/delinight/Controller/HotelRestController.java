package com.onetouch.delinight.Controller;

import com.onetouch.delinight.Service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hotel/rest")
@Log4j2
public class HotelRestController {
    private final HotelService hotelService;

    @DeleteMapping("/del")
    public void del(Long id){
        hotelService.del(id);

    }
}
