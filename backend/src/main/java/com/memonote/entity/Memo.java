package com.memonote.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("memo_note")
public class Memo implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private Long categoryId;
    
    private String title;
    
    private String content;
    
    private Integer priority;
    
    private Integer status;
    
    private LocalDateTime reminderTime;
    
    private String tags;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
    
    @TableField(exist = false)
    private String categoryName;
    
    @TableField(exist = false)
    private String categoryColor;
}
