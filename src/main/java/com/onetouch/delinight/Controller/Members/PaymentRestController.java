package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.Constant.PaidCheck;
import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.DTO.PaymentDTO;
import com.onetouch.delinight.Service.MembersService;
import com.onetouch.delinight.Service.PaymentService;
import com.onetouch.delinight.Util.ExcelUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentRestController {

    private final PaymentService paymentService;
    private final MembersService membersService;

    @PostMapping("/process")
    public List<PaymentDTO> processPayments(@RequestBody List<PaymentDTO> paymentDTOList) {
        // 1. 서비스 호출 후 결제처리 된 결제 내역처리
        List<PaymentDTO> processedPayment = paymentService.processPayments(paymentDTOList);
        // 2. 처리된 결제 내역 반환
        return processedPayment;
    }

    @GetMapping("/totalPrice")
    public ResponseEntity<?> paymentTotalPrice(
            @RequestParam(value = "paidCheck") PaidCheck paidCheck,
            @RequestParam(value = "startDate") String startDate1,
            @RequestParam(value = "endDate") String endDate1,
            @RequestParam(value = "download", required = false, defaultValue = "false") Boolean download,
            Principal principal){



        log.info(startDate1 + " " + endDate1);
        LocalDate startDate = LocalDate.parse(startDate1);
        LocalDate endDate = LocalDate.parse(endDate1);
        log.info(startDate + " " + endDate);


         try {

             log.info(startDate);
             log.info(endDate);
             // 1. 현재 로그인한 사용자 정보 가져오기
             String admin = principal.getName();
             // 2. 이메일로 회원 정보 조회
             MembersDTO membersDTO = membersService.findByEmail(admin);
             Long memberId = membersDTO.getId();
             log.info("회원 정보 : {}", membersDTO);
             // 3. 서비스 호출 : 가격 월, 타입, 매장 ID, 결제 상태로 필터링된 결제 정보 조회
             List<PaymentDTO> paymentDTOList = paymentService.paymentByCriteria(paidCheck, memberId, startDate, endDate);
             // 4. 조회된 데이터가 없다면
             if (paymentDTOList.isEmpty()) {
                 log.info("조회된 데이터가 없습니다.");
                 return new ResponseEntity<>(HttpStatus.NO_CONTENT);
             }
             // 후처리 된 결제 내역
             List<PaymentDTO> processedPayment = paymentService.processPayments(paymentDTOList);
             log.info("결제 내역 후처리 완료");
             // 6. 다운로드 요청일 경우 엑셀 파일로 반환
             if (download) {
                 ByteArrayInputStream excelStream = ExcelUtil.ExcelToStream(processedPayment);
                 HttpHeaders headers = new HttpHeaders();
                 headers.add("Content-Disposition", "attachment; filename=payment_data.xlsx");
                 return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).body(new InputStreamResource(excelStream));
             }
             // 후처리 된 결제 내역 반환
             return new ResponseEntity<>(processedPayment, HttpStatus.OK);
         }catch (Exception e){
            log.info("결제 내역 조회 중 오류 발생");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
         }

    }

}
