package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Entity.CheckOutLogEntity;
import com.onetouch.delinight.Entity.NetPromoterScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CheckOutLogRepository extends JpaRepository<CheckOutLogEntity, Long> {

        List<CheckOutLogEntity> findByCheckoutDate(LocalDate checkoutDate);

        NetPromoterScoreEntity findNetPromoterScoreByCheckOutLogEntity(CheckOutLogEntity checkOutLogEntity);


}
