package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Entity.ImageEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Transactional
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

    public Optional<ImageEntity> findByHotelEntity_Id(Long id);
    public void deleteByRegTimeIsLessThanEqualAndMenuEntityIsNullAndHotelEntityIsNull(LocalDateTime time);
    public Optional<ImageEntity> findByMenuEntity_Id(Long menuEntityId);
    public Optional<ImageEntity> findByStoreEntity_Id(Long storeEntityId);
    public Optional<ImageEntity> findByRoomCareMenuEntity_Id(Long menuId);

    public void deleteByMenuEntity_Id(Long menuEntityId);
    public void deleteByStoreEntity_Id(Long storeEntityId);
    public void deleteByRoomCareMenuEntity_Id(Long storeEntityId);
    public void deleteByHotelEntity_Id(Long hotelEntityId);

    public boolean existsByStoreEntity_Id(Long id);
    public boolean existsByHotelEntity_Id(Long id);

}
