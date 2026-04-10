package com.memonote.dto;

import lombok.Data;

@Data
public class CategoryRequest {
    private String name;
    
    private String color;
    
    private Integer sort;
}
