package com.onetouch.delinight.Service;

import com.onetouch.delinight.Entity.PaymentEntity;
import com.onetouch.delinight.Repository.PaymentRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Log4j2
class PaymentServiceImplTest {

    @Autowired
    PaymentRepository paymentRepository;

//    @Test
//    public void test(){
//        Long storeId = 1L;
//        List<PaymentEntity> b = paymentRepository.a(storeId);
//        log.info(b);
//    }

}