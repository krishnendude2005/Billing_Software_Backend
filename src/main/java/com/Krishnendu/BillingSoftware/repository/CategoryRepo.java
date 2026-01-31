package com.Krishnendu.BillingSoftware.repository;

import com.Krishnendu.BillingSoftware.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category, Long> {
    Optional<Category> findByCategoryId(String categoryId);
}
