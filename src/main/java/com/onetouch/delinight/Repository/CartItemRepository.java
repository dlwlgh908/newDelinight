package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {

    public Optional<CartItemEntity> findByCartEntity_IdAndMenuEntity_Id(Long cartEntityNum, Long menuEntityNum);
    public List<CartItemEntity> findByCartEntity_Id(Long CartEntityId);

    public void deleteByCartEntity_Id(Long CartNum);

}
