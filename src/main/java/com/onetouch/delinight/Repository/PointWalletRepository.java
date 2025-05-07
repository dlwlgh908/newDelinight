package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Entity.PointWalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointWalletRepository extends JpaRepository<PointWalletEntity,Long> {

    Optional <PointWalletEntity> findByUsersId(Long usersId);

    public PointWalletEntity deleteByUsersId(Long usersId);

    boolean existsByUsersId(Long usersId);
}
