package com.Krishnendu.BillingSoftware.controller;

import com.Krishnendu.BillingSoftware.io.CategoryRequest;
import com.Krishnendu.BillingSoftware.io.CategoryResponse;
import com.Krishnendu.BillingSoftware.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> addCategory(@RequestBody CategoryRequest categoryRequest) {

        CategoryResponse response = categoryService.add(categoryRequest);

        if(response.getCategoryId()==null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }

        return  ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> read() {
        List<CategoryResponse> response = categoryService.read();

        if(response==null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }

        return  ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
