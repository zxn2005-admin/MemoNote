package com.memonote.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.memonote.dto.MemoQueryRequest;
import com.memonote.dto.MemoRequest;
import com.memonote.entity.Memo;
import com.memonote.mapper.MemoMapper;
import com.memonote.service.MemoService;
import com.memonote.vo.MemoVO;
import com.memonote.vo.StatisticsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemoServiceImpl extends ServiceImpl<MemoMapper, Memo> implements MemoService {

    @Override
    public List<MemoVO> getMemoList(Long userId) {
        List<Memo> memos = baseMapper.selectMemoListWithCategory(userId);
        return memos.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public Page<MemoVO> getMemoPage(MemoQueryRequest request, Long userId) {
        LambdaQueryWrapper<Memo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Memo::getUserId, userId);
        
        if (StringUtils.hasText(request.getKeyword())) {
            wrapper.and(w -> w.like(Memo::getTitle, request.getKeyword())
                    .or()
                    .like(Memo::getContent, request.getKeyword()));
        }
        
        if (request.getCategoryId() != null) {
            wrapper.eq(Memo::getCategoryId, request.getCategoryId());
        }
        
        if (request.getStatus() != null) {
            wrapper.eq(Memo::getStatus, request.getStatus());
        }
        
        if (request.getPriority() != null) {
            wrapper.eq(Memo::getPriority, request.getPriority());
        }
        
        wrapper.orderByDesc(Memo::getCreateTime);
        
        Page<Memo> page = new Page<>(request.getPageNum(), request.getPageSize());
        Page<Memo> memoPage = page(page, wrapper);
        
        List<MemoVO> voList = memoPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        Page<MemoVO> resultPage = new Page<>();
        BeanUtils.copyProperties(memoPage, resultPage);
        resultPage.setRecords(voList);
        
        return resultPage;
    }

    @Override
    public MemoVO getMemoDetail(Long id, Long userId) {
        LambdaQueryWrapper<Memo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Memo::getId, id).eq(Memo::getUserId, userId);
        Memo memo = getOne(wrapper);
        
        if (memo == null) {
            throw new RuntimeException("备忘录不存在");
        }
        
        return convertToVO(memo);
    }

    @Override
    public void addMemo(MemoRequest request, Long userId) {
        Memo memo = new Memo();
        BeanUtils.copyProperties(request, memo);
        memo.setUserId(userId);
        memo.setStatus(0);
        save(memo);
    }

    @Override
    public void updateMemo(Long id, MemoRequest request, Long userId) {
        LambdaQueryWrapper<Memo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Memo::getId, id).eq(Memo::getUserId, userId);
        Memo memo = getOne(wrapper);
        
        if (memo == null) {
            throw new RuntimeException("备忘录不存在");
        }
        
        BeanUtils.copyProperties(request, memo);
        memo.setId(id);
        memo.setUserId(userId);
        updateById(memo);
    }

    @Override
    public void deleteMemo(Long id, Long userId) {
        LambdaQueryWrapper<Memo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Memo::getId, id).eq(Memo::getUserId, userId);
        if (!remove(wrapper)) {
            throw new RuntimeException("备忘录不存在");
        }
    }

    @Override
    public void toggleMemoStatus(Long id, Long userId) {
        LambdaQueryWrapper<Memo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Memo::getId, id).eq(Memo::getUserId, userId);
        Memo memo = getOne(wrapper);
        
        if (memo == null) {
            throw new RuntimeException("备忘录不存在");
        }
        
        memo.setStatus(memo.getStatus() == 0 ? 1 : 0);
        updateById(memo);
    }

    @Override
    public StatisticsVO getStatistics(Long userId) {
        LambdaQueryWrapper<Memo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Memo::getUserId, userId);
        
        long total = count(wrapper);
        
        LambdaQueryWrapper<Memo> completedWrapper = new LambdaQueryWrapper<>();
        completedWrapper.eq(Memo::getUserId, userId).eq(Memo::getStatus, 1);
        long completed = count(completedWrapper);
        
        LambdaQueryWrapper<Memo> pendingWrapper = new LambdaQueryWrapper<>();
        pendingWrapper.eq(Memo::getUserId, userId).eq(Memo::getStatus, 0);
        long pending = count(pendingWrapper);
        
        StatisticsVO statistics = new StatisticsVO();
        statistics.setTotalMemo(total);
        statistics.setCompletedMemo(completed);
        statistics.setPendingMemo(pending);
        
        return statistics;
    }

    private MemoVO convertToVO(Memo memo) {
        MemoVO vo = new MemoVO();
        BeanUtils.copyProperties(memo, vo);
        return vo;
    }
}
