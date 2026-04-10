package com.memonote.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.memonote.common.Result;
import com.memonote.entity.Memo;
import com.memonote.mapper.MemoMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MemoService extends ServiceImpl<MemoMapper, Memo> {

    public Result<Page<Memo>> getMemoList(Long userId, Long categoryId, String keyword, Integer page, Integer size) {
        LambdaQueryWrapper<Memo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Memo::getUserId, userId);

        if (categoryId != null && categoryId > 0) {
            wrapper.eq(Memo::getCategoryId, categoryId);
        }

        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(Memo::getTitle, keyword).or().like(Memo::getContent, keyword));
        }

        wrapper.orderByDesc(Memo::getIsTop)
                .orderByDesc(Memo::getCreateTime);

        Page<Memo> pageResult = page(new Page<>(page, size), wrapper);
        return Result.success(pageResult);
    }

    public Result<Memo> getMemoDetail(Long userId, Long id) {
        Memo memo = getById(id);
        if (memo == null || !memo.getUserId().equals(userId)) {
            return Result.error("备忘录不存在或无权限");
        }
        return Result.success(memo);
    }

    public Result<Memo> addMemo(Long userId, Memo memo) {
        memo.setUserId(userId);
        memo.setIsTop(false);
        memo.setIsComplete(false);
        save(memo);
        return Result.success(memo);
    }

    public Result<Void> updateMemo(Long userId, Long id, Memo memo) {
        Memo exist = getById(id);
        if (exist == null || !exist.getUserId().equals(userId)) {
            return Result.error("备忘录不存在或无权限");
        }
        memo.setId(id);
        memo.setUserId(userId);
        updateById(memo);
        return Result.success();
    }

    public Result<Void> deleteMemo(Long userId, Long id) {
        Memo exist = getById(id);
        if (exist == null || !exist.getUserId().equals(userId)) {
            return Result.error("备忘录不存在或无权限");
        }
        removeById(id);
        return Result.success();
    }

    public Result<Void> toggleTop(Long userId, Long id) {
        Memo exist = getById(id);
        if (exist == null || !exist.getUserId().equals(userId)) {
            return Result.error("备忘录不存在或无权限");
        }
        exist.setIsTop(!exist.getIsTop());
        updateById(exist);
        return Result.success();
    }

    public Result<Void> toggleComplete(Long userId, Long id) {
        Memo exist = getById(id);
        if (exist == null || !exist.getUserId().equals(userId)) {
            return Result.error("备忘录不存在或无权限");
        }
        exist.setIsComplete(!exist.getIsComplete());
        updateById(exist);
        return Result.success();
    }
}
