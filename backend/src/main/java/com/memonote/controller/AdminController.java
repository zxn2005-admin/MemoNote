package com.memonote.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.memonote.common.Result;
import com.memonote.entity.Memo;
import com.memonote.entity.User;
import com.memonote.mapper.MemoMapper;
import com.memonote.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private MemoMapper memoMapper;
    
    @GetMapping("/users")
    public Result<Map<String, Object>> getUserList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        
        Page<User> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(User::getCreateTime);
        
        IPage<User> pageResult = userMapper.selectPage(pageParam, wrapper);
        pageResult.getRecords().forEach(u -> u.setPassword(null));
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", pageResult.getRecords());
        result.put("total", pageResult.getTotal());
        
        return Result.success(result);
    }
    
    @PutMapping("/users/{id}/status")
    public Result<Void> updateUserStatus(@PathVariable Long id, @RequestParam Integer status) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        user.setStatus(status);
        userMapper.updateById(user);
        return Result.success("状态更新成功", null);
    }
    
    @GetMapping("/stats")
    public Result<Map<String, Object>> getSystemStats() {
        Map<String, Object> stats = new HashMap<>();
        
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getDeleted, 0);
        stats.put("totalUsers", userMapper.selectCount(userWrapper));
        
        LambdaQueryWrapper<Memo> memoWrapper = new LambdaQueryWrapper<>();
        memoWrapper.eq(Memo::getDeleted, 0);
        stats.put("totalMemos", memoMapper.selectCount(memoWrapper));
        
        return Result.success(stats);
    }
}
