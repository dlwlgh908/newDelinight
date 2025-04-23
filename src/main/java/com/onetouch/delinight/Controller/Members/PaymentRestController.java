package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.DTO.PaymentDTO;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Service.MembersService;
import com.onetouch.delinight.Service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentRestController {

    private final PaymentService paymentService;
    private final MembersService membersService;
    private final ModelMapper modelMapper;

    @GetMapping("/totalPrice")
    public ResponseEntity<List<PaymentDTO>> paymentTotalPrice(@RequestParam(value = "priceMonth", required = false) String priceMonth, @RequestParam(value = "type", required = false) String type, @RequestParam(value = "storeId", required = false) Long storeId, @RequestParam(value = "isPaid", required = false) Boolean isPaid, Principal principal) {

        try {
            // 1. 현재 로그인한 사용자의 정보 가져오기
            String admin = principal.getName();
            // 2. 이메일로 회원 정보 조회
            MembersDTO membersDTO = membersService.findByEmail(admin);
            log.info("membersDTO: " + membersDTO);
            // 3. 서비스 메서드 호출: 가격 월, 타입, 매장 ID, 결제 상태로 필터링된 결제 정보 조회
            List<PaymentDTO> paymentDTOList = paymentService.paymentByCriteria(priceMonth, null, storeId, isPaid, modelMapper.map(membersDTO, MembersEntity.class));
            // 4. 응답으로 반환
            if (paymentDTOList.isEmpty()) {
                log.info("paymentDTOList is empty");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 조회된 데이터가 없으면 204 No Content 반환
            }
            return new ResponseEntity<>(paymentDTOList, HttpStatus.OK); // 200 OK와 함께 데이터 반환
        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("sajkdhaklsdfghaslkfdaskljdgaslkgdaslgdasldgas");
            // 3. 예외 처리: 예외가 발생하면 500 Internal Server Error 반환
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }




}
