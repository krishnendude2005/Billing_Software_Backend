package com.Krishnendu.BillingSoftware.controller;

import com.Krishnendu.BillingSoftware.io.CategoryRequest;
import com.Krishnendu.BillingSoftware.io.CategoryResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @PostMapping("/add")
    public CategoryResponse addCategory(@RequestBody CategoryRequest categoryRequest) {

        return null;
    }
}
