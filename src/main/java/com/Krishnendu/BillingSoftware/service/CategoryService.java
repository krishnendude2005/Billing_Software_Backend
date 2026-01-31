package com.Krishnendu.BillingSoftware.service;

import com.Krishnendu.BillingSoftware.io.CategoryRequest;
import com.Krishnendu.BillingSoftware.io.CategoryResponse;

public interface CategoryService
{
    CategoryResponse add(CategoryRequest categoryRequest);
}
