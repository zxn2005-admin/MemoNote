package com.memonote.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryVO {
    private Long id;
    private String name;
    private String color;
    private String icon;
    private Integer sortOrder;
    private LocalDateTime createTime;
}
