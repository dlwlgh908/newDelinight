/*********************************************************************
 * 클래스명 : MembersService
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.PaidCheck;
import com.onetouch.delinight.DTO.ExcelDTO;
import com.onetouch.delinight.DTO.OrdersDTO;
import com.onetouch.delinight.DTO.PaymentDTO;

import java.time.LocalDate;
import java.util.List;

public interface PaymentService {

    public List<OrdersDTO> readOrders(Long paymentId);

    public List<PaymentDTO> paymentByCriteria(PaidCheck paidCheck, Long memberId, LocalDate startDate, LocalDate endDate);

    // 계산 메소드
    List<PaymentDTO> processPayments(List<PaymentDTO> paymentDTOList);

    List<ExcelDTO> extractData(List<PaymentDTO> paymentDTOList);


}
