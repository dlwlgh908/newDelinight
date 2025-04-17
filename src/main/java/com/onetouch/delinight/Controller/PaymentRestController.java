package com.onetouch.delinight.Controller;

import com.onetouch.delinight.Constant.PayType;
import com.onetouch.delinight.DTO.PaymentDTO;
import com.onetouch.delinight.Service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentRestController {

    private final PaymentService paymentService;

    @GetMapping("/allDate")
    public ResponseEntity <List<PaymentDTO>> allDate (@RequestParam("totalId")Long totalId, @RequestParam("type")PayType type){
        log.info("정산 요청 받음: totalId = {}, type = {}", totalId, type);
        log.info("정산 데이터 조회 시작: totalId = {}, type = {}", totalId, type);

        List<PaymentDTO> paymentDTOList = paymentService.findAllDate(totalId, type);

        if (paymentDTOList != null && !paymentDTOList.isEmpty()){
            log.info("정산 데이터 조회 완료, 결과: {} 건", paymentDTOList.size());
        }else{
            log.info("정산 데이터가 없습니다.");
        }

        log.info("정산 데이터 반환 준비 완료, 총 {} 건", paymentDTOList.size());

        return new ResponseEntity<>(paymentDTOList, HttpStatus.OK);
    }

}
