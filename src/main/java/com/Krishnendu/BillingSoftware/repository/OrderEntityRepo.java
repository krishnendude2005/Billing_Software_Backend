package com.Krishnendu.BillingSoftware.repository;

import com.Krishnendu.BillingSoftware.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderEntityRepo extends JpaRepository<OrderEntity, Long> {

    Optional<OrderEntity>findByOrderId(String orderId);
    List<OrderEntity> findAllByOrderByCreatedAtDesc();
}
