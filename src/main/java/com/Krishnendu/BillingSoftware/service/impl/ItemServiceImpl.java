package com.Krishnendu.BillingSoftware.service.impl;

import com.Krishnendu.BillingSoftware.entity.Category;
import com.Krishnendu.BillingSoftware.entity.ItemEntity;
import com.Krishnendu.BillingSoftware.io.ItemRequest;
import com.Krishnendu.BillingSoftware.io.ItemResponse;
import com.Krishnendu.BillingSoftware.repository.CategoryRepo;
import com.Krishnendu.BillingSoftware.repository.ItemRepo;
import com.Krishnendu.BillingSoftware.service.FileUploadService;
import com.Krishnendu.BillingSoftware.service.ItemService;
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
public class ItemServiceImpl implements ItemService {

    private final ItemRepo itemRepo;
    private final FileUploadService fileUploadService;
    private final CategoryRepo categoryRepo;

    @Override
    public ItemResponse add(ItemRequest request, MultipartFile file) {
        String fileName = UUID.randomUUID().toString() + "." + StringUtils.getFilenameExtension(file.getOriginalFilename());
        Path uploadPath = Paths.get("uploads").toAbsolutePath().normalize();

        //String imgUrl = fileUploadService.uploadFile(file); ------------AWS S3------------
        try {
            Files.createDirectories(uploadPath);
            Path targetLocation = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            String imgUrl = "http://localhost:8080/api/v1.0/uploads/" + fileName;
            ItemEntity newItem = convertToEntity(request);
            newItem.setImageUrl(imgUrl);

            // Set the category
            Category belongingCategory = categoryRepo.findByCategoryId(request.getCategoryId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

            newItem.setCategory(belongingCategory);

            newItem = itemRepo.save(newItem);
            return convertToResponse(newItem);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload image");
        }

    }

    private ItemResponse convertToResponse(ItemEntity newItem) {
        return ItemResponse.builder()
                .itemId(newItem.getItemId())
                .name(newItem.getName())
                .description(newItem.getDescription())
                .price(newItem.getPrice())
                .categoryId(newItem.getCategory().getCategoryId())
                .categoryName(newItem.getCategory().getName())
                .imageUrl(newItem.getImageUrl())
                .createdAt(newItem.getCreatedAt())
                .updatedAt(newItem.getUpdatedAt())
                .build();
    }

    private ItemEntity convertToEntity(ItemRequest request) {
        return ItemEntity.builder()
                .itemId(UUID.randomUUID().toString())
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .build();

    }

    @Override
    public List<ItemResponse> fetchItems() {
        return itemRepo.findAll().stream()
                .map(item -> convertToResponse(item))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteItem(String itemId) {

        ItemEntity existingItem = itemRepo.findByItemId(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found with ID :" + itemId));

        //---------AWS S3----------------------------
//        // 1. Delete the image from AWS
//        boolean isFileDeleted = fileUploadService.deleteFile(existingItem.getImageUrl());
//        if (!isFileDeleted) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting file from aws");
//        }
        //------------------------------------------------
        String imgUrl = existingItem.getImageUrl();
        String fileName = imgUrl.substring(imgUrl.lastIndexOf('/') + 1);
        Path uploadPath = Paths.get("uploads").toAbsolutePath().normalize();
        Path filePath = uploadPath.resolve(fileName);
        try {
            Files.deleteIfExists(filePath);

            // 2. Delete the item from DB
            itemRepo.delete(existingItem);
            System.out.println("Item deleted successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
