package com.memonote.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.memonote.common.Result;
import com.memonote.entity.User;
import com.memonote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/list")
    public Result<Page<User>> getUserList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(User::getUsername, keyword).or().like(User::getEmail, keyword);
        }
        wrapper.orderByDesc(User::getCreateTime);
        Page<User> pageResult = userService.page(new Page<>(page, size), wrapper);
        pageResult.getRecords().forEach(u -> u.setPassword(null));
        return Result.success(pageResult);
    }

    @PutMapping("/user/update-role/{id}")
    public Result<Void> updateUserRole(@PathVariable Long id, @RequestBody User user) {
        User updateUser = new User();
        updateUser.setId(id);
        updateUser.setRole(user.getRole());
        userService.updateById(updateUser);
        return Result.success();
    }

    @DeleteMapping("/user/delete/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.removeById(id);
        return Result.success();
    }

    @GetMapping("/statistics")
    public Result<Object> getStatistics() {
        long userCount = userService.count();
        return Result.success(new java.util.HashMap<String, Object>() {{
            put("userCount", userCount);
        }});
    }
}
