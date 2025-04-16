package com.onetouch.delinight.Controller;

import com.onetouch.delinight.Constant.OrderType;
import com.onetouch.delinight.Constant.PaidCheck;
import com.onetouch.delinight.DTO.PaymentDTO;
import com.onetouch.delinight.Service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentRestController {

    private final PaymentService paymentService;

    @GetMapping("/storePayment")
    public List<PaymentDTO> storePayment(@RequestParam("storeId") Long storeId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate, @RequestParam String orderType, @RequestParam String paidCheck) {
        System.out.println("storeId = " + storeId);
        System.out.println("startDate = " + startDate);
        System.out.println("endDate = " + endDate);
        System.out.println("orderType = " + orderType);
        System.out.println("paidCheck = " + paidCheck);
        OrderType ot;
        PaidCheck pc;

        if(paidCheck.equals("paid")) {
            pc = PaidCheck.paid;
        }else {
            pc = PaidCheck.none;
        }

        if (orderType.equals("PAYNOW")) {
            ot = OrderType.PAYNOW;
        }else {
            ot = OrderType.PAYLATER;
        }

        return paymentService.selectSettlementPaymentList(storeId, startDate, endDate, ot, pc);
    }

}
