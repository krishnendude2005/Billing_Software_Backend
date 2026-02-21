package com.Krishnendu.BillingSoftware.service.impl;

import com.Krishnendu.BillingSoftware.entity.Category;
import com.Krishnendu.BillingSoftware.io.CategoryRequest;
import com.Krishnendu.BillingSoftware.io.CategoryResponse;
import com.Krishnendu.BillingSoftware.repository.CategoryRepo;
import com.Krishnendu.BillingSoftware.repository.ItemRepo;
import com.Krishnendu.BillingSoftware.service.CategoryService;
import com.Krishnendu.BillingSoftware.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;
    private final FileUploadService fileUploadService;
    private final ItemRepo itemRepo;

    @Override
    public CategoryResponse add(CategoryRequest categoryRequest, MultipartFile file) {

        try {
            String imgUrl = fileUploadService.uploadFile(file); //<-----AWS S3---------

            Category newCategory = convertToEntity(categoryRequest);
            newCategory.setImageUrl(imgUrl);

            newCategory = categoryRepo.save(newCategory);
            return convertToResponse(newCategory);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to upload image");
        }
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

        Category existingCategory = categoryRepo.findByCategoryId(categoryId).orElseThrow(() -> new RuntimeException("Category not found : " + categoryId));


        // delete from S3 first
        Boolean ans = fileUploadService.deleteFile(existingCategory.getImageUrl());
        if (ans) {
            System.out.println("Delete file successfully");
        }

        // delete it from DB second
        categoryRepo.delete(existingCategory);

    }

    private CategoryResponse convertToResponse(Category newCategory) {
        int itemsCount = itemRepo.countByCategoryId(newCategory.getId());
        return CategoryResponse.builder()
                .categoryId(newCategory.getCategoryId())
                .name(newCategory.getName())
                .description(newCategory.getDescription())
                .bgColor(newCategory.getBgColor())
                .imageUrl(newCategory.getImageUrl())
                .createdAt(newCategory.getCreatedAt())
                .updatedAt(newCategory.getUpdatedAt())
                .items(itemsCount)
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
