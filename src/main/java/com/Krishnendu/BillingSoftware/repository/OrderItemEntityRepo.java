package com.Krishnendu.BillingSoftware.repository;

import com.Krishnendu.BillingSoftware.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemEntityRepo extends JpaRepository<OrderItemEntity, Long> {
}
