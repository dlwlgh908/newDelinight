/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.Role;
import com.onetouch.delinight.DTO.CheckInDTO;
import com.onetouch.delinight.DTO.OrdersDTO;
import com.onetouch.delinight.DTO.PaymentDTO;
import com.onetouch.delinight.DTO.StoreDTO;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Entity.OrdersEntity;
import com.onetouch.delinight.Entity.PaymentEntity;
import com.onetouch.delinight.Repository.CustomPaymentRepositoryImpl;
import com.onetouch.delinight.Repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final CustomPaymentRepositoryImpl customPaymentRepository;
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

    @Override   // 계산식은 이후 자바스크립트로 처리할 예정
    public List<PaymentDTO> paymentByCriteria(String priceMonth, String type, Long storeId, Boolean isPaid, MembersEntity member) {
        Role role = member.getRole();
        // SYSTEMADMIN, SUPERADMIN은 모든 결제 내역 조회 가능
        if (role == Role.SYSTEMADMIN || role == Role.SUPERADMIN){
            log.info("관리자 권한으로 모든 결제 내역 조회중");
        }
        // ADMIN, STOREADMIN은 특정 매장에 대한 결제 내역만 조회 가능
        if (role == Role.ADMIN || role == Role.STOREADMIN){
            log.info("호텔 관리자 권한으로 매장 결제 내역 조회");
        }
        List<PaymentDTO> paymentDTOList = customPaymentRepository.findPaymentByCriteria(priceMonth, null, storeId, isPaid);
        log.info("레포지토리에서 {}개의 PaymentDTO를 조회함.", paymentDTOList.size());
        return paymentDTOList;
    }





}
