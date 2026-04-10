package com.memonote.controller;

import com.memonote.common.Result;
import com.memonote.entity.User;
import com.memonote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public Result<User> getUserInfo(@AuthenticationPrincipal Long userId) {
        return userService.getUserInfo(userId);
    }

    @PutMapping("/update")
    public Result<Void> updateUser(@AuthenticationPrincipal Long userId, @RequestBody User user) {
        return userService.updateUser(userId, user);
    }
}
