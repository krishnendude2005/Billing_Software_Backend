package com.Krishnendu.BillingSoftware.repository;

import com.Krishnendu.BillingSoftware.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Long> {
}
