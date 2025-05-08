package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.PointType;
import com.onetouch.delinight.Entity.PointLogEntity;
import com.onetouch.delinight.Entity.PointWalletEntity;
import com.onetouch.delinight.Entity.UsersEntity;
import com.onetouch.delinight.Repository.PaymentRepository;
import com.onetouch.delinight.Repository.PointLogRepository;
import com.onetouch.delinight.Repository.PointWalletRepository;
import com.onetouch.delinight.Repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.module.ResolutionException;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class PointServiceImpl implements PointService{

    private final PointWalletRepository pointWalletRepository;
    private final PointLogRepository pointLogRepository;
    private final UsersRepository usersRepository;
    private final PaymentRepository paymentRepository;


    //포인트 사용 처리
    @Override
    public void usePoint(Long usersId, Long paymentId, int usedPoint) {

        //유저가 실제 가입된 회원인지 먼저 확인
        UsersEntity users = usersRepository.findById(usersId)
                .orElseThrow(()-> new IllegalArgumentException("회원이 아닙니다. 포인트 사용 불가"));

        //사용자 ID로 포인트 지갑을 DB에서 찾는다. 없으면 예외를 던진다 ("지갑이 없습니다").
        PointWalletEntity wallet = pointWalletRepository.findByUsersId(usersId)
                .orElseThrow(()-> new ResolutionException("포인트 지갑이 없습니다"));

        //보유 포인트가 요청한 사용 포인트보다 적으면 "포인트 부족" 예외 발생시킨다.
        if (wallet.getCPoint() < usedPoint){

            log.error("[포인트 사용 실패] 포인트 부족 - 보유: {}, 요청: {}", wallet.getCPoint(), usedPoint);
            throw new ResolutionException("포인트가 부족합니다.");
        }

        //사용한 포인트만큼 지갑에서 포인트를 차감한다.
        wallet.setCPoint(wallet.getCPoint() - usedPoint);
        //변경된 지갑 상태(차감된 포인트)를 DB에 저장한다.
        pointWalletRepository.save(wallet);
        log.info("[포인트 차감 완료] 차감 후 포인트: {}", wallet.getCPoint());


        //포인트 사용 로그를 새로 만든다. (사용 타입, 어떤 결제에 사용했는지, 몇 포인트 썼는지)
        PaymentEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(()-> new IllegalArgumentException("결제 정보가 존재하지 않습니다."));

        PointLogEntity log = PointLogEntity.builder()
                .pointType(PointType.USE) //포인트 사용 로그
                .payment(paymentRepository.findById(paymentId).orElseThrow()) //결제 id로 결제 정보 조회
                .pointAmount(usedPoint) //사용한 포인트 양 기록
                .build();

        //만든 로그를 DB에 저장한다.
        pointLogRepository.save(log);


    }


    //포인트 적립
    @Override
    public void earnPoint(Long usersId, Long paymentId, int paymentAmount) {
        //결제 금액의 10%를 포인트로 적립할 양으로 계산한다.
        int earnPoint = (int) (paymentAmount * 0.1);

        //유저인지
        UsersEntity users = usersRepository.findById(usersId)
                .orElseThrow(()-> new IllegalArgumentException("회원이 아닙니다. 포인트 사용 불가"));

        // 사용자 ID로 포인트 지갑을 찾는다. 없으면 예외 발생.
        PointWalletEntity wallet = pointWalletRepository.findByUsersId(usersId)
                .orElseThrow(()-> new ResolutionException("포인트 지갑이 없습니다."));

        //결제 금액의 10%를 포인트로 적립할 양으로 계산한다.
        int earnPoint = (int) (paymentAmount * 0.1);
        log.info("[적립 포인트 계산] 적립 포인트: {}", earnPoint);


        //계산된 적립 포인트만큼 지갑에 추가한다.
        wallet.setCPoint(wallet.getCPoint() + earnPoint); //적립
        //변경된 지갑 상태를 저장한다 (포인트 적립 반영).
        pointWalletRepository.save(wallet);
        log.info("[포인트 적립 완료] 적립 후 포인트: {}", wallet.getCPoint());


        //포인트 적립 로그를 생성한다.
        PaymentEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(()-> new IllegalArgumentException("결제 정보가 존재하지 않습니다."));

        PointLogEntity log = PointLogEntity.builder()
                .pointType(PointType.EARN) //포인트 적립 타입
                .payment(paymentRepository.findById(paymentId).orElseThrow()) // 결제 정보 조회
                .pointAmount(earnPoint) //적집 포인트 양 기록
                .build();

        //만든 로그를 DB에 저장한다.
        pointLogRepository.save(log);

    }

    //회원가입 시 지갑 생성
    @Override
    public void createWallet(Long usersId) {

        //유저 존재 여부 확인
        UsersEntity users = usersRepository.findById(usersId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 회원입니다."));

        //이미 지갑이 있다면 중복 생성 방지
        if (pointWalletRepository.existsByUsersId(usersId)){
            throw new IllegalArgumentException("이미 포인트 지갑이 존재합니다.");
        }


        //새 포인트 지갑을 만든다. 이 유저에게, 0포인트로.
        PointWalletEntity wallet = PointWalletEntity.builder()
                .users(users)//유저와 연결
                .cPoint(0L) //처음엔 0포인 트
                .build();

        // 만든 지갑을 DB에 저장한다.
        pointWalletRepository.save(wallet);

    }

    //회원탈퇴 시 지갑 삭제
    @Override
    public void deleteWallet(Long usersId) {

        //유저 존재 여부 확인
        if (!usersRepository.existsById(usersId)) {
            throw new IllegalStateException("존재하지 않는 회원입니다. 지갑 삭제 불가.");
        }
        //해당 유저 ID로 포인트 지갑을 삭제한다.
        pointWalletRepository.deleteByUsersId(usersId);
        log.info("[지갑 삭제 완료] 사용자 ID: {}", usersId);


    }

    //결제를 처리하면서 포인트 사용 여부에 따라 자동으로 포인트 적립 여부를 판단
    public void processPaymentWithPoint(Long usersId, Long paymentId, int paymentAmount, int usedPoint) {

        // 유저 유효성 확인
        UsersEntity user = usersRepository.findById(usersId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 아닙니다."));

        // 결제 내역 조회
        PaymentEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("결제 정보가 없습니다."));

        // 포인트가 사용된 경우
        if (usedPoint > 0) {
            // 포인트 사용 로직 실행 (내부에서 잔액 체크 및 로그 처리 포함)
            usePoint(usersId, paymentId, usedPoint);

            // 결제 금액에서 포인트를 차감 (DB에 저장)
            payment.setAmount(paymentAmount - usedPoint);
            paymentRepository.save(payment);

            log.info("[결제 처리] 포인트 사용 후 결제 금액: {}", payment.getAmount());
        } else {
            // 포인트 사용 안 한 경우: 결제 금액 그대로 저장
            payment.setAmount(paymentAmount);
            paymentRepository.save(payment);

            // 포인트 적립 처리
            earnPoint(usersId, paymentId, paymentAmount);

            log.info("[결제 처리] 포인트 사용 안함 - 10% 적립 완료");
        }
    }






}
