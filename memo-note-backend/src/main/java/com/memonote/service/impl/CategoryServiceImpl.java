package com.memonote.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.memonote.dto.CategoryRequest;
import com.memonote.entity.Category;
import com.memonote.mapper.CategoryMapper;
import com.memonote.service.CategoryService;
import com.memonote.vo.CategoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public List<CategoryVO> getAllCategories() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSortOrder);
        List<Category> categories = list(wrapper);
        
        return categories.stream().map(category -> {
            CategoryVO vo = new CategoryVO();
            BeanUtils.copyProperties(category, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public void addCategory(CategoryRequest request) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getName, request.getName());
        if (getOne(wrapper) != null) {
            throw new RuntimeException("分类名称已存在");
        }

        Category category = new Category();
        BeanUtils.copyProperties(request, category);
        save(category);
    }

    @Override
    public void updateCategory(Long id, CategoryRequest request) {
        Category category = getById(id);
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }

        if (!category.getName().equals(request.getName())) {
            LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Category::getName, request.getName());
            if (getOne(wrapper) != null) {
                throw new RuntimeException("分类名称已存在");
            }
        }

        BeanUtils.copyProperties(request, category);
        category.setId(id);
        updateById(category);
    }

    @Override
    public void deleteCategory(Long id) {
        if (getById(id) == null) {
            throw new RuntimeException("分类不存在");
        }
        removeById(id);
    }
}
