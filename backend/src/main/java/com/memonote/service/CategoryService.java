package com.memonote.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.memonote.common.Result;
import com.memonote.entity.Category;
import com.memonote.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {

    public Result<List<Category>> getCategoryList(Long userId) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getUserId, userId)
                .orderByAsc(Category::getSort)
                .orderByDesc(Category::getCreateTime);
        List<Category> list = list(wrapper);
        return Result.success(list);
    }

    public Result<Category> addCategory(Long userId, Category category) {
        category.setUserId(userId);
        save(category);
        return Result.success(category);
    }

    public Result<Void> updateCategory(Long userId, Long id, Category category) {
        Category exist = getById(id);
        if (exist == null || !exist.getUserId().equals(userId)) {
            return Result.error("分类不存在或无权限");
        }
        category.setId(id);
        category.setUserId(userId);
        updateById(category);
        return Result.success();
    }

    public Result<Void> deleteCategory(Long userId, Long id) {
        Category exist = getById(id);
        if (exist == null || !exist.getUserId().equals(userId)) {
            return Result.error("分类不存在或无权限");
        }
        removeById(id);
        return Result.success();
    }
}
