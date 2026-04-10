package com.memonote.controller;

import com.memonote.common.Result;
import com.memonote.entity.User;
import com.memonote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/profile")
    public Result<User> getProfile(@AuthenticationPrincipal Long userId) {
        User user = userService.getCurrentUser(userId);
        user.setPassword(null);
        return Result.success(user);
    }
    
    @PutMapping("/profile")
    public Result<Void> updateProfile(@AuthenticationPrincipal Long userId, @RequestBody User userUpdate) {
        try {
            userService.updateProfile(userId, userUpdate);
            return Result.success("更新成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PutMapping("/password")
    public Result<Void> updatePassword(@AuthenticationPrincipal Long userId, @RequestBody Map<String, String> params) {
        try {
            String oldPassword = params.get("oldPassword");
            String newPassword = params.get("newPassword");
            userService.updatePassword(userId, oldPassword, newPassword);
            return Result.success("密码修改成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/stats")
    public Result<Map<String, Object>> getUserStats(@AuthenticationPrincipal Long userId) {
        Map<String, Object> stats = new HashMap<>();
        User user = userService.getCurrentUser(userId);
        stats.put("user", user);
        return Result.success(stats);
    }
}
