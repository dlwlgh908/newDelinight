/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.CheckInDTO;
import com.onetouch.delinight.DTO.OrdersDTO;
import com.onetouch.delinight.DTO.StoreDTO;
import com.onetouch.delinight.Entity.OrdersEntity;
import com.onetouch.delinight.Entity.PaymentEntity;
import com.onetouch.delinight.Repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
