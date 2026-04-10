package com.memonote.dto;

import lombok.Data;

@Data
public class MemoQueryRequest {
    private String keyword;
    private Long categoryId;
    private Integer status;
    private Integer priority;
    private Long pageNum = 1L;
    private Long pageSize = 10L;
}
