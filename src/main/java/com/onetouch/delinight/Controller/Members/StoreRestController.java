package com.onetouch.delinight.Controller.Members;


import com.onetouch.delinight.DTO.MenuDTO;
import com.onetouch.delinight.DTO.OrdersDTO;
import com.onetouch.delinight.Service.OrdersService;

import com.onetouch.delinight.Util.MemberDetails;

import com.onetouch.delinight.Service.StoreService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
@Log4j2
public class StoreRestController {

    private final OrdersService ordersService;
    private final StoreService storeService;

    @GetMapping("/orders/processingList")
    public ResponseEntity<Page<OrdersDTO>> processingList(@PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, @AuthenticationPrincipal MemberDetails memberDetails){


        String email = memberDetails.getUsername();
        Page<OrdersDTO> ordersDTOPage = ordersService.processList(pageable, email);
        return ResponseEntity.ok(ordersDTOPage);
    }

    @GetMapping("/orders/completeList")
    public ResponseEntity<Page<OrdersDTO>> completeList(@PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, Principal principal) {

        String email = "hansin@a.a";
        Page<OrdersDTO> ordersDTOPage = ordersService.completeList(pageable, email);
        return ResponseEntity.ok(ordersDTOPage);
    }

    @PostMapping("/orders/changeStatus")
    public ResponseEntity<String> changeStatus(Long ordersId, String ordersStatus) {
        ordersService.changeStatus(ordersId, ordersStatus);
        return ResponseEntity.ok("변경 완료");
    }

    @DeleteMapping("/rest/del")
    public void del(Long id) {
        storeService.del(id);
    }
  
}
