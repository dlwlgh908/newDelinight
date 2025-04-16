/*********************************************************************
 * 클래스명 : MembersService
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.OrderType;
import com.onetouch.delinight.Constant.PaidCheck;
import com.onetouch.delinight.DTO.OrdersDTO;
import com.onetouch.delinight.DTO.PaymentDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentService {

    public List<OrdersDTO> readOrders(Long paymentId);

    // 정산을 위한 결제 리스트 조회
    List<PaymentDTO> selectSettlementPaymentList(Long storeId, LocalDateTime startDate, LocalDateTime endDate, OrderType orderType, PaidCheck paidCheck);

}
