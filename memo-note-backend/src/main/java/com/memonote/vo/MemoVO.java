package com.memonote.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemoVO {
    private Long id;
    private Long userId;
    private Long categoryId;
    private String title;
    private String content;
    private Integer priority;
    private Integer status;
    private LocalDateTime remindTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String categoryName;
    private String categoryColor;
}
