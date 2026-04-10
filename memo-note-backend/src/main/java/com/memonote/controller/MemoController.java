package com.memonote.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.memonote.common.Result;
import com.memonote.dto.MemoQueryRequest;
import com.memonote.dto.MemoRequest;
import com.memonote.security.SecurityUtil;
import com.memonote.service.MemoService;
import com.memonote.vo.MemoVO;
import com.memonote.vo.StatisticsVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memo")
public class MemoController {

    @Autowired
    private MemoService memoService;

    @GetMapping("/list")
    public Result<List<MemoVO>> getMemoList() {
        Long userId = SecurityUtil.getCurrentUserId();
        List<MemoVO> list = memoService.getMemoList(userId);
        return Result.success(list);
    }

    @GetMapping("/page")
    public Result<Page<MemoVO>> getMemoPage(MemoQueryRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        Page<MemoVO> page = memoService.getMemoPage(request, userId);
        return Result.success(page);
    }

    @GetMapping("/{id}")
    public Result<MemoVO> getMemoDetail(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        MemoVO memoVO = memoService.getMemoDetail(id, userId);
        return Result.success(memoVO);
    }

    @PostMapping
    public Result<Void> addMemo(@Valid @RequestBody MemoRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        memoService.addMemo(request, userId);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> updateMemo(@PathVariable Long id, @Valid @RequestBody MemoRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        memoService.updateMemo(id, request, userId);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteMemo(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        memoService.deleteMemo(id, userId);
        return Result.success();
    }

    @PutMapping("/{id}/toggle")
    public Result<Void> toggleMemoStatus(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        memoService.toggleMemoStatus(id, userId);
        return Result.success();
    }

    @GetMapping("/statistics")
    public Result<StatisticsVO> getStatistics() {
        Long userId = SecurityUtil.getCurrentUserId();
        StatisticsVO statistics = memoService.getStatistics(userId);
        return Result.success(statistics);
    }
}
