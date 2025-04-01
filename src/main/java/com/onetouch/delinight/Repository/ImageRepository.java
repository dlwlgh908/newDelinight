package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Entity.ImageEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

@Transactional
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

    public void deleteByRegTimeIsLessThanEqualAndMenuEntityIsNullAndHotelEntityIsNull(LocalDateTime time);


}
