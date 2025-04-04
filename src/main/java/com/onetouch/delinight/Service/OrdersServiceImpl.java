/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.*;
import com.onetouch.delinight.Entity.OrdersEntity;
import com.onetouch.delinight.Entity.PaymentEntity;
import com.onetouch.delinight.Repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService{

    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<OrdersDTO> read(Long paymentNum) {

        PaymentEntity paymentEntity = paymentRepository.findById(paymentNum).get();
        List<OrdersEntity> ordersEntityList = paymentEntity.getOrdersEntityList();
        List<OrdersDTO> ordersDTOList = ordersEntityList.stream().map(data-> modelMapper.map(data, OrdersDTO.class)
                .setOrdersItemDTOList(data.getOrdersItemEntities().stream().map(data2->modelMapper.map(data2, OrdersItemDTO.class)
                        .setOrdersDTO(modelMapper.map(data2.getOrdersEntity(), OrdersDTO.class)).setMenuDTO(modelMapper.map(data2.getMenuEntity(),MenuDTO.class)
                                )).toList())
                .setStoreDTO(modelMapper.map(data.getStoreEntity(),StoreDTO.class))
                .setCheckInDTO(modelMapper.map(data.getCheckInEntity(), CheckInDTO.class))).toList();

        return ordersDTOList;
    }
}
