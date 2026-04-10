package com.memonote.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemoRequest {
    private Long id;
    
    private Long categoryId;
    
    @NotBlank(message = "标题不能为空")
    private String title;
    
    private String content;
    
    private Integer priority;
    
    private Integer status;
    
    private LocalDateTime remindTime;
}
