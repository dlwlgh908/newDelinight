/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.OrderType;
import com.onetouch.delinight.Constant.PaidCheck;
import com.onetouch.delinight.DTO.CheckInDTO;
import com.onetouch.delinight.DTO.OrdersDTO;
import com.onetouch.delinight.DTO.PaymentDTO;
import com.onetouch.delinight.DTO.StoreDTO;
import com.onetouch.delinight.Entity.OrdersEntity;
import com.onetouch.delinight.Entity.PaymentEntity;
import com.onetouch.delinight.Repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<OrdersDTO> readOrders(Long paymentId) {

        PaymentEntity paymentEntity = paymentRepository.findById(paymentId).get();
        List<OrdersEntity> ordersEntityList = paymentEntity.getOrdersEntityList();
        List<OrdersDTO> ordersDTOList = ordersEntityList.stream()
                .map(data->modelMapper.map(data,OrdersDTO.class)
                        .setStoreDTO(modelMapper.map(data.getStoreEntity(), StoreDTO.class))
                        .setCheckInDTO(modelMapper.map(data.getCheckInEntity(), CheckInDTO.class))).toList();

        return ordersDTOList;
    }

    // 1. 조건에 맞는 결제 목록을 DB 조회
    // 2. Entity → DTO 변환
    // paymentEntityList.stream() : 리스트를 스트림으로 순회
    // .map(entity -> modelMapper.map(...)) : 각 PaymentEntity 객체를 PaymentDTO 변환
    // .toList() : 스트림을 다시 리스트로 변환해서 담음
    // 3. 변환된 DTOList 반환
    @Override
    public List<PaymentDTO> selectSettlementPaymentList(Long storeId, LocalDateTime startDate, LocalDateTime endDate, OrderType orderType, PaidCheck paidCheck){
        List<PaymentEntity> paymentEntityList = paymentRepository.findPaymentsForSettlement(storeId, startDate, endDate, orderType, paidCheck);
        List<PaymentDTO> paymentDTOList = paymentEntityList.stream().map(entity -> modelMapper.map(entity, PaymentDTO.class)).toList();
        return paymentDTOList;
    }






}
