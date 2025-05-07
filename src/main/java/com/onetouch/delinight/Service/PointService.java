package com.onetouch.delinight.Service;

import com.onetouch.delinight.Entity.UsersEntity;

public interface PointService {


    //포인트 사용 처리
    public void usePoint(Long usersId, Long paymentId, int usedPoint);

    //포인트 적립
    public void earnPoint(Long usersId, Long paymentId, int paymentAmount);


    //회원가입 시 지갑 생성
    public void createWallet(Long usersId);

    //회원탈퇴 시 지갑 삭제
    public void deleteWallet(Long usersId);

    //결제를 처리하면서 포인트 사용 여부에 따라 자동으로 포인트 적립 여부를 판단
    public void processPaymentWithPoint(Long usersId, Long paymentId, int paymentAmount, int usedPoint);

}
