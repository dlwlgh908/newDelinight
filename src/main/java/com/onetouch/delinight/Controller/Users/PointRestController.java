package com.onetouch.delinight.Controller.Users;

import com.onetouch.delinight.Service.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/point")
@RequiredArgsConstructor
@Log4j2
public class PointRestController {

    private final PointService pointService;

    //결제 시 포인트 사용
    @PostMapping("/use")
    public ResponseEntity<String> uesPoint(@RequestParam Long usersId, @RequestParam Long paymentId,@RequestParam int usedPoint){

        log.info("[포인트 사용] 사용자 ID: {}, 결제 ID: {}, 사용 포인트: {}", usersId, paymentId, usedPoint);
        //포인트 사용 로직 실행
        pointService.usePoint(usersId, paymentId, usedPoint);

        log.info("[포인트 사용 완료] 사용자 ID: {}", usersId);
        //성공 응답
        return ResponseEntity.ok("포인트 사용 완료");
    }

    //결제 후 포인트 적립 (포인트 사용 안한 경우)
    @PostMapping("/earn")
    public ResponseEntity<String> earnPoint(@RequestParam Long usersId,@RequestParam Long paymentId,@RequestParam int paymentAmount){

        log.info("[포인트 적립 시작] 사용자 ID: {}, 결제 ID: {}, 결제 금액: {}", usersId, paymentId, paymentAmount);
        //포인트 적립 로직 실행
        pointService.earnPoint(usersId, paymentId, paymentAmount);

        log.info("[포인트 적립 완료] 사용자 ID: {}, 적립 포인트: {}", usersId, (int)(paymentAmount * 0.1));
        //성공 응답
        return ResponseEntity.ok("포인트 적립 완료");
    }

    //회원가입 시 지갑 생성
    @PostMapping("/create-wallet")
    public ResponseEntity<String> createWallet(@RequestParam Long usersId) {

        log.info("[지갑 생성 요청] 사용자 ID: {}", usersId);
        // 지갑 생성
        pointService.createWallet(usersId);

        log.info("[지갑 생성 완료] 사용자 ID: {}", usersId);
        return ResponseEntity.ok("포인트 지갑 생성 완료");
    }

    //회원탈퇴 시 지갑 삭제
    @DeleteMapping("/delete-wallet")
    public ResponseEntity<String> deleteWallet(@RequestParam Long usersId) {

        log.info("[지갑 삭제 요청] 사용자 ID: {}", usersId);
        // 지갑 삭제
        pointService.deleteWallet(usersId);

        log.info("[지갑 삭제 완료] 사용자 ID: {}", usersId);
        return ResponseEntity.ok("포인트 지갑 삭제 완료");
    }

    @PostMapping("/process-payment")
    public ResponseEntity<String> processPayment(
            @RequestParam Long usersId,
            @RequestParam Long paymentId,
            @RequestParam int paymentAmount,
            @RequestParam(defaultValue = "0") int usedPoint
    ) {//사용자 id, 결제id, 결제금액, 포인트 사용량
        log.info("[결제 요청] 사용자 ID: {}, 결제 ID: {}, 결제 금액: {}, 사용 포인트: {}", usersId, paymentId, paymentAmount, usedPoint);
        //실제 결제 처리와 포인트 관련 처리
        //포인트 차감및 적립하거나 결제 정보를 저장
        pointService.processPaymentWithPoint(usersId, paymentId, paymentAmount, usedPoint);
        return ResponseEntity.ok("결제 및 포인트 처리 완료");
    }


}
