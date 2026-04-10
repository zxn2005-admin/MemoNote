package com.memonote.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {
    private Long id;
    
    @NotBlank(message = "分类名称不能为空")
    private String name;
    
    private String color;
    
    private String icon;
    
    private Integer sortOrder;
}
