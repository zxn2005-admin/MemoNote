package com.memonote.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.memonote.dto.CategoryRequest;
import com.memonote.entity.Category;
import com.memonote.vo.CategoryVO;

import java.util.List;

public interface CategoryService extends IService<Category> {
    List<CategoryVO> getAllCategories();
    void addCategory(CategoryRequest request);
    void updateCategory(Long id, CategoryRequest request);
    void deleteCategory(Long id);
}
