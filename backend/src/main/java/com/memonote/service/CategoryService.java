package com.memonote.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.memonote.dto.CategoryRequest;
import com.memonote.entity.Category;
import com.memonote.mapper.CategoryMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {
    
    public List<Category> getUserCategories(Long userId) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getUserId, userId)
               .orderByAsc(Category::getSort)
               .orderByDesc(Category::getCreateTime);
        return this.list(wrapper);
    }
    
    public Category createCategory(Long userId, CategoryRequest request) {
        Category category = new Category();
        category.setUserId(userId);
        category.setName(request.getName());
        category.setColor(request.getColor() != null ? request.getColor() : "#409EFF");
        category.setSort(request.getSort() != null ? request.getSort() : 0);
        this.save(category);
        return category;
    }
    
    public Category updateCategory(Long userId, Long categoryId, CategoryRequest request) {
        Category category = this.getById(categoryId);
        if (category == null || !category.getUserId().equals(userId)) {
            throw new RuntimeException("分类不存在或无权限");
        }
        if (request.getName() != null) {
            category.setName(request.getName());
        }
        if (request.getColor() != null) {
            category.setColor(request.getColor());
        }
        if (request.getSort() != null) {
            category.setSort(request.getSort());
        }
        this.updateById(category);
        return category;
    }
    
    public void deleteCategory(Long userId, Long categoryId) {
        Category category = this.getById(categoryId);
        if (category == null || !category.getUserId().equals(userId)) {
            throw new RuntimeException("分类不存在或无权限");
        }
        this.removeById(categoryId);
    }
}
