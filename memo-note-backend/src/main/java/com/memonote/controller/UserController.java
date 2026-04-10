package com.memonote.controller;

import com.memonote.common.Result;
import com.memonote.dto.UserUpdateRequest;
import com.memonote.security.SecurityUtil;
import com.memonote.service.UserService;
import com.memonote.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public Result<UserVO> getProfile() {
        Long userId = SecurityUtil.getCurrentUserId();
        UserVO userVO = userService.getCurrentUser(userId);
        return Result.success(userVO);
    }

    @PutMapping("/profile")
    public Result<Void> updateProfile(@RequestBody UserUpdateRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        userService.updateUser(userId, request);
        return Result.success();
    }
}
