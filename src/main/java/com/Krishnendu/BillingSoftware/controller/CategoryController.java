package com.Krishnendu.BillingSoftware.controller;

import com.Krishnendu.BillingSoftware.io.CategoryRequest;
import com.Krishnendu.BillingSoftware.io.CategoryResponse;
import com.Krishnendu.BillingSoftware.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> addCategory(@RequestPart("category") String categoryRequestString,
                                                        @RequestPart("file") MultipartFile file
                                                        ) {

        // map the incoming string as the category request
        ObjectMapper mapper = new ObjectMapper();

        CategoryRequest categoryRequest = null;
        try {
            categoryRequest = mapper.readValue(categoryRequestString, CategoryRequest.class);
            CategoryResponse response = categoryService.add(categoryRequest, file);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while parsing incoming request string as the request object" + e.getMessage());
        }

    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> read() {
        List<CategoryResponse> response = categoryService.read();

        if (response == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable String categoryId) {
        try {
            categoryService.delete(categoryId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

        return
                ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .body(null);
    }
}
