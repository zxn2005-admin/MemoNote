package com.memonote.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.memonote.common.Result;
import com.memonote.entity.Memo;
import com.memonote.service.MemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/memo")
public class MemoController {

    @Autowired
    private MemoService memoService;

    @GetMapping("/list")
    public Result<Page<Memo>> getMemoList(
            @AuthenticationPrincipal Long userId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return memoService.getMemoList(userId, categoryId, keyword, page, size);
    }

    @GetMapping("/detail/{id}")
    public Result<Memo> getMemoDetail(@AuthenticationPrincipal Long userId, @PathVariable Long id) {
        return memoService.getMemoDetail(userId, id);
    }

    @PostMapping("/add")
    public Result<Memo> addMemo(@AuthenticationPrincipal Long userId, @RequestBody Memo memo) {
        return memoService.addMemo(userId, memo);
    }

    @PutMapping("/update/{id}")
    public Result<Void> updateMemo(@AuthenticationPrincipal Long userId, @PathVariable Long id, @RequestBody Memo memo) {
        return memoService.updateMemo(userId, id, memo);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteMemo(@AuthenticationPrincipal Long userId, @PathVariable Long id) {
        return memoService.deleteMemo(userId, id);
    }

    @PutMapping("/toggle-top/{id}")
    public Result<Void> toggleTop(@AuthenticationPrincipal Long userId, @PathVariable Long id) {
        return memoService.toggleTop(userId, id);
    }

    @PutMapping("/toggle-complete/{id}")
    public Result<Void> toggleComplete(@AuthenticationPrincipal Long userId, @PathVariable Long id) {
        return memoService.toggleComplete(userId, id);
    }
}
