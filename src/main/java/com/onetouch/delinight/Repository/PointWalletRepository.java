package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Entity.PointWalletEntity;
import com.onetouch.delinight.Entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.method.P;

import java.util.Optional;

public interface PointWalletRepository extends JpaRepository<PointWalletEntity,Long> {

    Optional <PointWalletEntity> findByUsersId(Long usersId);

    public PointWalletEntity deleteByUsersId(Long usersId);
}
