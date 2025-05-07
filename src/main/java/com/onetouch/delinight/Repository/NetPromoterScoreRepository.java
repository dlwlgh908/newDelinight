package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Entity.HotelEntity;
import com.onetouch.delinight.Entity.NetPromoterScoreEntity;
import com.onetouch.delinight.Entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NetPromoterScoreRepository extends JpaRepository<NetPromoterScoreEntity , Long> {

    List<NetPromoterScoreEntity> findAll();
    List<NetPromoterScoreEntity> findByHotelEntityOrStoreEntityIn(HotelEntity hotelEntity, List<StoreEntity> storeEntities);
    List<NetPromoterScoreEntity> findByStoreEntity(StoreEntity storeEntity);

}
