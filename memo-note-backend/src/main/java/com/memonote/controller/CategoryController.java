package com.memonote.controller;

import com.memonote.common.Result;
import com.memonote.dto.CategoryRequest;
import com.memonote.service.CategoryService;
import com.memonote.vo.CategoryVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public Result<List<CategoryVO>> getAllCategories() {
        List<CategoryVO> list = categoryService.getAllCategories();
        return Result.success(list);
    }

    @PostMapping
    public Result<Void> addCategory(@Valid @RequestBody CategoryRequest request) {
        categoryService.addCategory(request);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        categoryService.updateCategory(id, request);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.success();
    }
}
