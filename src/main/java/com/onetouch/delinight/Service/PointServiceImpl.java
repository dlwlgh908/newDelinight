package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.PaidCheck;
import com.onetouch.delinight.Constant.PointType;
import com.onetouch.delinight.DTO.OrdersDTO;
import com.onetouch.delinight.DTO.PaymentDTO;
import com.onetouch.delinight.Entity.PaymentEntity;
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
import java.time.LocalDate;
import java.util.List;

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

        //사용자 ID로 포인트 지갑을 DB에서 찾는다. 없으면 예외를 던진다 ("지갑이 없습니다").
        PointWalletEntity wallet = pointWalletRepository.findByUsersId(usersId)
                .orElseThrow(()-> new ResolutionException("지갑이 없습니다"));

        //보유 포인트가 요청한 사용 포인트보다 적으면 "포인트 부족" 예외 발생시킨다.
        if (wallet.getCPoint() < usedPoint){
            throw new ResolutionException("포인트가 부족합니다.");
        }

        //사용한 포인트만큼 지갑에서 포인트를 차감한다.
        wallet.setCPoint(wallet.getCPoint() - usedPoint);
        //변경된 지갑 상태(차감된 포인트)를 DB에 저장한다.
        pointWalletRepository.save(wallet);

        //포인트 사용 로그를 새로 만든다. (사용 타입, 어떤 결제에 사용했는지, 몇 포인트 썼는지)
        PointLogEntity log = PointLogEntity.builder()
                .pointType(PointType.USE) //사용 로그
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

        // 사용자 ID로 포인트 지갑을 찾는다. 없으면 예외 발생.
        PointWalletEntity wallet = pointWalletRepository.findByUsersId(usersId)
                .orElseThrow(()-> new ResolutionException("지갑이 없습니다."));

        //계산된 적립 포인트만큼 지갑에 추가한다.
        wallet.setCPoint(wallet.getCPoint() + earnPoint);
        //변경된 지갑 상태를 저장한다 (포인트 적립 반영).
        pointWalletRepository.save(wallet);

        //포인트 적립 로그를 생성한다.
        PointLogEntity log = PointLogEntity.builder()
                .pointType(PointType.EARN) //적립 로그
                .payment(paymentRepository.findById(paymentId).orElseThrow()) // 결제 정보 조회
                .pointAmount(earnPoint) //적집 포인트 양 기록
                .build();

        //만든 로그를 DB에 저장한다.
        pointLogRepository.save(log);

    }

    //회원가입 시 지갑 생성
    @Override
    public void createWallet(Long userId) {

        // 사용자 ID로 유저 정보를 조회한다. 없으면 예외 발생.
        UsersEntity user = usersRepository.findById(userId).orElseThrow();

        //새 포인트 지갑을 만든다. 이 유저에게, 0포인트로.
        PointWalletEntity wallet = PointWalletEntity.builder()
                .users(user)
                .cPoint(0L)
                .build();

        // 만든 지갑을 DB에 저장한다.
        pointWalletRepository.save(wallet);

    }

    //회원탈퇴 시 지갑 삭제
    @Override
    public void deleteWallet(Long usersId) {

        //해당 유저 ID로 포인트 지갑을 삭제한다.
        pointWalletRepository.deleteByUsersId(usersId);

    }
}
