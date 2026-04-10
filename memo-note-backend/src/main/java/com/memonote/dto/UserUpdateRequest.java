package com.memonote.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserUpdateRequest {
    private String nickname;
    
    @Email(message = "邮箱格式不正确")
    private String email;
    
    private String avatar;
    
    private String oldPassword;
    
    private String newPassword;
}
