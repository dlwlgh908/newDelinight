package com.onetouch.delinight.Repository;

import com.onetouch.delinight.DTO.PaymentDTO;

import java.util.List;

public interface CustomPaymentRepository {

     List<PaymentDTO> findPaymentByCriteria(String priceMonth, Long storeId, Boolean isPaid, Long memberId);


}
