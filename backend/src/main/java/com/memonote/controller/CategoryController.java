package com.memonote.controller;

import com.memonote.common.Result;
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
        return categoryService.getCategoryList(userId);
    }

    @PostMapping("/add")
    public Result<Category> addCategory(@AuthenticationPrincipal Long userId, @RequestBody Category category) {
        return categoryService.addCategory(userId, category);
    }

    @PutMapping("/update/{id}")
    public Result<Void> updateCategory(@AuthenticationPrincipal Long userId, @PathVariable Long id, @RequestBody Category category) {
        return categoryService.updateCategory(userId, id, category);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteCategory(@AuthenticationPrincipal Long userId, @PathVariable Long id) {
        return categoryService.deleteCategory(userId, id);
    }
}
