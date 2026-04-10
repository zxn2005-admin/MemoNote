package com.memonote.vo;

import lombok.Data;

@Data
public class StatisticsVO {
    private Long totalMemo;
    private Long completedMemo;
    private Long pendingMemo;
    private Long totalCategory;
}
