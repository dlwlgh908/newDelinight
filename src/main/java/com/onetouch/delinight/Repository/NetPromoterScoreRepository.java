package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Entity.CheckOutLogEntity;
import com.onetouch.delinight.Entity.NetPromoterScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NetPromoterScoreRepository extends JpaRepository<NetPromoterScoreEntity , Long> {

    public List<NetPromoterScoreEntity> findByCheckOutLogEntityId(Long checkOutId);

    NetPromoterScoreEntity findByCheckOutLogEntity(List<CheckOutLogEntity> checkOutLogEntity);
}
