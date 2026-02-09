package com.Krishnendu.BillingSoftware.repository;

import com.Krishnendu.BillingSoftware.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepo extends JpaRepository<ItemEntity, Long> {
    Optional<ItemEntity> findByItemId(String ItemId);
    Integer countByCategoryId(Long id);
}
