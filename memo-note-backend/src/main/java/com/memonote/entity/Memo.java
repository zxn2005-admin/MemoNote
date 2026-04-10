package com.memonote.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("memo")
public class Memo {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("user_id")
    private Long userId;
    
    @TableField("category_id")
    private Long categoryId;
    
    private String title;
    private String content;
    
    @TableField("priority")
    private Integer priority;
    
    @TableField("status")
    private Integer status;
    
    @TableField("remind_time")
    private LocalDateTime remindTime;
    
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableField(exist = false)
    private String categoryName;
    
    @TableField(exist = false)
    private String categoryColor;
}
