package com.memonote.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MemoRequest {
    private Long categoryId;
    
    private String title;
    
    private String content;
    
    private Integer priority;
    
    private Integer status;
    
    private LocalDateTime reminderTime;
    
    private String tags;
}
