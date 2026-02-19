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

        // String imgUrl = fileUploadService.uploadFile(file); <-----AWS S3---------

        String fileName = UUID.randomUUID().toString() + "." + StringUtils.getFilenameExtension(file.getOriginalFilename());
        Path uploadPath = Paths.get("uploads").toAbsolutePath().normalize();
        try {
            Files.createDirectories(uploadPath);
            Path targetLocation = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            String imgUrl = "http://localhost:8080/api/v1.0/uploads/" + fileName;


            Category newCategory = convertToEntity(categoryRequest);
            newCategory.setImageUrl(imgUrl);

            newCategory = categoryRepo.save(newCategory);
            return convertToResponse(newCategory);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create directory for uploading files");
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
        // delete from DB
        Category existingCategory = categoryRepo.findByCategoryId(categoryId).orElseThrow(() -> new RuntimeException("Category not found : " + categoryId));
        categoryRepo.delete(existingCategory);

        // delete from S3 also
//        Boolean ans = fileUploadService.deleteFile(existingCategory.getImageUrl());
//        if (ans) {
//            System.out.println("Delete file successfully");
//        }


        String imgUrl = existingCategory.getImageUrl();
        String fileName = imgUrl.substring(imgUrl.lastIndexOf('/') + 1);
        Path uploadPath = Paths.get("uploads").toAbsolutePath().normalize();
        Path filePath = uploadPath.resolve(fileName);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
