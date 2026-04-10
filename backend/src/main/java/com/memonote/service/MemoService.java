package com.memonote.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.memonote.dto.MemoRequest;
import com.memonote.entity.Memo;
import com.memonote.mapper.MemoMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;

@Service
public class MemoService extends ServiceImpl<MemoMapper, Memo> {
    
    public IPage<Memo> getUserMemos(Long userId, Integer page, Integer size, Long categoryId, Integer status, Integer priority) {
        Page<Memo> pageParam = new Page<>(page, size);
        
        LambdaQueryWrapper<Memo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Memo::getUserId, userId);
        
        if (categoryId != null) {
            wrapper.eq(Memo::getCategoryId, categoryId);
        }
        if (status != null) {
            wrapper.eq(Memo::getStatus, status);
        }
        if (priority != null) {
            wrapper.eq(Memo::getPriority, priority);
        }
        
        wrapper.orderByDesc(Memo::getCreateTime);
        
        return this.page(pageParam, wrapper);
    }
    
    public IPage<Memo> searchMemos(Long userId, String keyword, Integer page, Integer size) {
        Page<Memo> pageParam = new Page<>(page, size);
        return baseMapper.searchByKeyword(pageParam, userId, keyword);
    }
    
    public Memo getMemoById(Long userId, Long memoId) {
        Memo memo = this.getById(memoId);
        if (memo == null || !memo.getUserId().equals(userId)) {
            throw new RuntimeException("备忘录不存在或无权限");
        }
        return memo;
    }
    
    public Memo createMemo(Long userId, MemoRequest request) {
        Memo memo = new Memo();
        memo.setUserId(userId);
        memo.setCategoryId(request.getCategoryId());
        memo.setTitle(request.getTitle());
        memo.setContent(request.getContent());
        memo.setPriority(request.getPriority() != null ? request.getPriority() : 0);
        memo.setStatus(request.getStatus() != null ? request.getStatus() : 0);
        memo.setReminderTime(request.getReminderTime());
        memo.setTags(request.getTags());
        this.save(memo);
        return memo;
    }
    
    public Memo updateMemo(Long userId, Long memoId, MemoRequest request) {
        Memo memo = this.getMemoById(userId, memoId);
        
        if (request.getCategoryId() != null) {
            memo.setCategoryId(request.getCategoryId());
        }
        if (request.getTitle() != null) {
            memo.setTitle(request.getTitle());
        }
        if (request.getContent() != null) {
            memo.setContent(request.getContent());
        }
        if (request.getPriority() != null) {
            memo.setPriority(request.getPriority());
        }
        if (request.getStatus() != null) {
            memo.setStatus(request.getStatus());
        }
        if (request.getReminderTime() != null) {
            memo.setReminderTime(request.getReminderTime());
        }
        if (request.getTags() != null) {
            memo.setTags(request.getTags());
        }
        
        this.updateById(memo);
        return memo;
    }
    
    public void deleteMemo(Long userId, Long memoId) {
        Memo memo = this.getMemoById(userId, memoId);
        this.removeById(memoId);
    }
    
    public void updateStatus(Long userId, Long memoId, Integer status) {
        Memo memo = this.getMemoById(userId, memoId);
        memo.setStatus(status);
        this.updateById(memo);
    }
    
    public long countByUserId(Long userId) {
        LambdaQueryWrapper<Memo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Memo::getUserId, userId);
        return this.count(wrapper);
    }
    
    public long countByUserIdAndStatus(Long userId, Integer status) {
        LambdaQueryWrapper<Memo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Memo::getUserId, userId)
               .eq(Memo::getStatus, status);
        return this.count(wrapper);
    }
}
