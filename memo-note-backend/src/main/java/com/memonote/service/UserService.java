package com.memonote.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.memonote.dto.LoginRequest;
import com.memonote.dto.RegisterRequest;
import com.memonote.dto.UserUpdateRequest;
import com.memonote.entity.User;
import com.memonote.vo.LoginVO;
import com.memonote.vo.UserVO;

public interface UserService extends IService<User> {
    LoginVO login(LoginRequest request);
    void register(RegisterRequest request);
    UserVO getCurrentUser(Long userId);
    void updateUser(Long userId, UserUpdateRequest request);
}
