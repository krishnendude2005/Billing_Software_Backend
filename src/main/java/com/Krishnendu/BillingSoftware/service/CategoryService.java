package com.Krishnendu.BillingSoftware.service;

import com.Krishnendu.BillingSoftware.io.CategoryRequest;
import com.Krishnendu.BillingSoftware.io.CategoryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService
{
    CategoryResponse add(CategoryRequest categoryRequest, MultipartFile file);
    List<CategoryResponse> read();
    void delete(String categoryId);
}
