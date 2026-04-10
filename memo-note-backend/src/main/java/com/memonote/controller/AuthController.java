package com.memonote.controller;

import com.memonote.common.Result;
import com.memonote.dto.LoginRequest;
import com.memonote.dto.RegisterRequest;
import com.memonote.security.SecurityUtil;
import com.memonote.service.UserService;
import com.memonote.vo.LoginVO;
import com.memonote.vo.UserVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginRequest request) {
        LoginVO loginVO = userService.login(request);
        return Result.success(loginVO);
    }

    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return Result.success();
    }

    @GetMapping("/info")
    public Result<UserVO> getUserInfo() {
        Long userId = SecurityUtil.getCurrentUserId();
        UserVO userVO = userService.getCurrentUser(userId);
        return Result.success(userVO);
    }
}
