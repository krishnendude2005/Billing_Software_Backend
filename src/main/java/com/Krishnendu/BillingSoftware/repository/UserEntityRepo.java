package com.Krishnendu.BillingSoftware.repository;

import com.Krishnendu.BillingSoftware.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserEntityRepo extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity>findByEmail(String email);
    Optional<UserEntity>findByUserId(String userId);

}
