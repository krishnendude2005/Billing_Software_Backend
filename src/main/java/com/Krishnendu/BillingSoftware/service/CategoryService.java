package com.Krishnendu.BillingSoftware.service;

import com.Krishnendu.BillingSoftware.io.CategoryRequest;
import com.Krishnendu.BillingSoftware.io.CategoryResponse;

import java.util.List;

public interface CategoryService
{
    CategoryResponse add(CategoryRequest categoryRequest);
    List<CategoryResponse> read();
}
