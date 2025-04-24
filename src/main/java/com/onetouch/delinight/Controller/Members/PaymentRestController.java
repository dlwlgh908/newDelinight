package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.DTO.PaymentDTO;
import com.onetouch.delinight.Service.MembersService;
import com.onetouch.delinight.Service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentRestController {

    private final PaymentService paymentService;
    private final MembersService membersService;

    @GetMapping("/criteria")
    public List<PaymentDTO> paymentCriteria(
            @RequestParam(required = false) String priceMonth,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) Boolean isPaid,
            @RequestParam Long memberId){
            // 1. 서비스 메서드 호출후 결제 내역을 조회
            List<PaymentDTO> paymentDTOList = paymentService.paymentByCriteria(priceMonth, type, storeId, isPaid, memberId);
            // 2. 조회된 결제 내역을 반환
            return paymentDTOList;
    }

    @PostMapping("/process")
    public List<PaymentDTO> processPayments(@RequestBody List<PaymentDTO> paymentDTOList) {
        // 1. 서비스 호출 후 결제처리 된 결제 내역처리
        List<PaymentDTO> processedPayment = paymentService.processPayments(paymentDTOList);
        // 2. 처리된 결제 내역 반환
        return processedPayment;
    }

    @GetMapping("/totalPrice")
    public ResponseEntity<?> paymentTotalPrice(
            @RequestParam(value = "priceMonth", required = false) String priceMonth,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "storeId", required = false) Long storeId,
            @RequestParam(value = "isPaid", required = false) Boolean isPaid,
            @RequestParam(value = "download", required = false, defaultValue = "false") Boolean download,
            Principal principal
    ){

        log.info("priceMonth: {}, type: {}, storeId: {}, isPaid: {}, download: {}, principal: {}", priceMonth, type, storeId, isPaid, download, principal);

         try {
             // 1. 현재 로그인한 사용자 정보 가져오기
             String admin = principal.getName();

             // 2. 이메일로 회원 정보 조회
             MembersDTO membersDTO = membersService.findByEmail(admin);
             Long memberId = membersDTO.getId();
             log.info("회원 정보 : {}", membersDTO);

             // 3. 서비스 호출 : 가격 월, 타입, 매장 ID, 결제 상태로 필터링된 결제 정보 조회
             List<PaymentDTO> paymentDTOList = paymentService.paymentByCriteria(priceMonth, type, storeId, isPaid, memberId);

             // 4. 조회된 데이터가 없다면
             if (paymentDTOList.isEmpty()) {
                 log.info("조회된 데이터가 없습니다.");
                 return new ResponseEntity<>(HttpStatus.NO_CONTENT);
             }

             // 후처리 된 결제 내역
             List<PaymentDTO> processedPayment = paymentService.processPayments(paymentDTOList);
             log.info("결제 내역 후처리 완료");

             // 후처리 된 결제 내역 반환
             return new ResponseEntity<>(processedPayment, HttpStatus.OK);

         }catch (Exception e){

            log.info("결제 내역 조회 중 오류 발생");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

         }

    }


}
