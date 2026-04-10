package com.memonote.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.memonote.common.Result;
import com.memonote.dto.MemoRequest;
import com.memonote.entity.Memo;
import com.memonote.service.MemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/memo")
public class MemoController {
    
    @Autowired
    private MemoService memoService;
    
    @GetMapping("/list")
    public Result<Map<String, Object>> getMemoList(
            @AuthenticationPrincipal Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer priority) {
        
        IPage<Memo> pageResult = memoService.getUserMemos(userId, page, size, categoryId, status, priority);
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", pageResult.getRecords());
        result.put("total", pageResult.getTotal());
        result.put("page", pageResult.getCurrent());
        result.put("size", pageResult.getSize());
        
        return Result.success(result);
    }
    
    @GetMapping("/search")
    public Result<Map<String, Object>> searchMemos(
            @AuthenticationPrincipal Long userId,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        
        IPage<Memo> pageResult = memoService.searchMemos(userId, keyword, page, size);
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", pageResult.getRecords());
        result.put("total", pageResult.getTotal());
        result.put("page", pageResult.getCurrent());
        result.put("size", pageResult.getSize());
        
        return Result.success(result);
    }
    
    @GetMapping("/{id}")
    public Result<Memo> getMemo(@AuthenticationPrincipal Long userId, @PathVariable Long id) {
        try {
            Memo memo = memoService.getMemoById(userId, id);
            return Result.success(memo);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping
    public Result<Memo> createMemo(@AuthenticationPrincipal Long userId, @RequestBody MemoRequest request) {
        try {
            Memo memo = memoService.createMemo(userId, request);
            return Result.success("创建成功", memo);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public Result<Memo> updateMemo(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id,
            @RequestBody MemoRequest request) {
        try {
            Memo memo = memoService.updateMemo(userId, id, request);
            return Result.success("更新成功", memo);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> deleteMemo(@AuthenticationPrincipal Long userId, @PathVariable Long id) {
        try {
            memoService.deleteMemo(userId, id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id,
            @RequestParam Integer status) {
        try {
            memoService.updateStatus(userId, id, status);
            return Result.success("状态更新成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/stats")
    public Result<Map<String, Object>> getMemoStats(@AuthenticationPrincipal Long userId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", memoService.countByUserId(userId));
        stats.put("completed", memoService.countByUserIdAndStatus(userId, 1));
        stats.put("pending", memoService.countByUserIdAndStatus(userId, 0));
        return Result.success(stats);
    }
}
