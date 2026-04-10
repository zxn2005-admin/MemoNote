package com.memonote.controller;

import com.memonote.common.Result;
import com.memonote.dto.CategoryRequest;
import com.memonote.entity.Category;
import com.memonote.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    @GetMapping("/list")
    public Result<List<Category>> getCategoryList(@AuthenticationPrincipal Long userId) {
        List<Category> categories = categoryService.getUserCategories(userId);
        return Result.success(categories);
    }
    
    @PostMapping
    public Result<Category> createCategory(@AuthenticationPrincipal Long userId, @RequestBody CategoryRequest request) {
        try {
            Category category = categoryService.createCategory(userId, request);
            return Result.success("创建成功", category);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public Result<Category> updateCategory(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id,
            @RequestBody CategoryRequest request) {
        try {
            Category category = categoryService.updateCategory(userId, id, request);
            return Result.success("更新成功", category);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> deleteCategory(@AuthenticationPrincipal Long userId, @PathVariable Long id) {
        try {
            categoryService.deleteCategory(userId, id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
