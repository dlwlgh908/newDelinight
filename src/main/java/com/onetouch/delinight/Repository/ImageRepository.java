package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Entity.ImageEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Transactional
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

    public void deleteByRegTimeIsLessThanEqualAndMenuEntityIsNullAndHotelEntityIsNull(LocalDateTime time);
    public Optional<ImageEntity> findByMenuEntity_Id(Long menuEntityId);
    public Optional<ImageEntity> findByStoreEntity_Id(Long storeEntityId);

    public void deleteByMenuEntity_Id(Long menuEntityId);
    public void deleteByStoreEntity_Id(Long storeEntityId);

}
