package com.Krishnendu.BillingSoftware.service.impl;

import com.Krishnendu.BillingSoftware.entity.Category;
import com.Krishnendu.BillingSoftware.io.CategoryRequest;
import com.Krishnendu.BillingSoftware.io.CategoryResponse;
import com.Krishnendu.BillingSoftware.repository.CategoryRepo;
import com.Krishnendu.BillingSoftware.service.CategoryService;
import com.Krishnendu.BillingSoftware.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;
    private final FileUploadService fileUploadService;

    @Override
    public CategoryResponse add(CategoryRequest categoryRequest, MultipartFile file) {

        String imgUrl = fileUploadService.uploadFile(file);

        Category newCategory = convertToEntity(categoryRequest);
        newCategory.setImageUrl(imgUrl);

        newCategory = categoryRepo.save(newCategory);
        return convertToResponse(newCategory);
    }

    @Override
    public List<CategoryResponse> read() {
        return
                categoryRepo.findAll()
                        .stream()
                        .map(category -> convertToResponse(category))
                        .collect(Collectors.toList());
    }

    @Override
    public void delete(String categoryId) {
        // delete from DB
        Category existingCategory = categoryRepo.findByCategoryId(categoryId).orElseThrow(() -> new RuntimeException("Category not found : " + categoryId));
        categoryRepo.delete(existingCategory);

        // delete from S3 also
        Boolean ans = fileUploadService.deleteFile(existingCategory.getImageUrl());
        if(ans) {
            System.out.println("Delete file successfully");
        }
    }

    private CategoryResponse convertToResponse(Category newCategory) {
        return CategoryResponse.builder()
                .categoryId(newCategory.getCategoryId())
                .name(newCategory.getName())
                .description(newCategory.getDescription())
                .bgColor(newCategory.getBgColor())
                .imageUrl(newCategory.getImageUrl())
                .createdAt(newCategory.getCreatedAt())
                .updatedAt(newCategory.getUpdatedAt())
                .build();
    }

    private Category convertToEntity(CategoryRequest categoryRequest) {
        return Category.builder()
                .categoryId(UUID.randomUUID().toString())
                .name(categoryRequest.getName())
                .description(categoryRequest.getDescription())
                .bgColor(categoryRequest.getBgColor())
                .build();
    }
}
