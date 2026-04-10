package com.memonote.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.memonote.common.Result;
import com.memonote.entity.User;
import com.memonote.service.UserService;
import com.memonote.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public Result<List<UserVO>> getAllUsers() {
        List<User> users = userService.list();
        List<UserVO> voList = users.stream().map(user -> {
            UserVO vo = new UserVO();
            BeanUtils.copyProperties(user, vo);
            return vo;
        }).collect(Collectors.toList());
        return Result.success(voList);
    }

    @PutMapping("/users/{id}/status")
    public Result<Void> updateUserStatus(@PathVariable Long id, @RequestParam Integer status) {
        User user = userService.getById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setStatus(status);
        userService.updateById(user);
        return Result.success();
    }

    @DeleteMapping("/users/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.removeById(id);
        return Result.success();
    }

    @GetMapping("/statistics")
    public Result<java.util.Map<String, Object>> getAdminStatistics() {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        
        long totalUsers = userService.count();
        long totalMemos = userService.count();
        
        LambdaQueryWrapper<User> adminWrapper = new LambdaQueryWrapper<>();
        adminWrapper.eq(User::getRole, 1);
        long adminCount = userService.count(adminWrapper);
        
        stats.put("totalUsers", totalUsers);
        stats.put("totalMemos", totalMemos);
        stats.put("adminCount", adminCount);
        
        return Result.success(stats);
    }
}
