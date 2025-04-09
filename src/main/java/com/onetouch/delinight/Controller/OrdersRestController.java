package com.onetouch.delinight.Controller;


import com.onetouch.delinight.DTO.OrdersDTO;
import com.onetouch.delinight.Service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roomService/order")
@Log4j2
public class OrdersRestController {

    private final OrdersService ordersService;


    @PostMapping("/payNow")
    public ResponseEntity<String> payNow(Long ordersId, String memo){
        log.info("선결제 진입");
        ordersService.changePayNow(ordersId, memo);
        return ResponseEntity.ok("저장 성공");
    }


    @PostMapping("/payLater")
    public ResponseEntity<String> payLater(Long ordersId, String memo){
        log.info("후결제 진입");
        ordersService.changePayLater(ordersId, memo);
        return ResponseEntity.ok("저장 성공");
    }
}
