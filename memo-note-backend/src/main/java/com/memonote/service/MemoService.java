package com.memonote.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.memonote.dto.MemoQueryRequest;
import com.memonote.dto.MemoRequest;
import com.memonote.entity.Memo;
import com.memonote.vo.MemoVO;
import com.memonote.vo.StatisticsVO;

import java.util.List;

public interface MemoService extends IService<Memo> {
    List<MemoVO> getMemoList(Long userId);
    Page<MemoVO> getMemoPage(MemoQueryRequest request, Long userId);
    MemoVO getMemoDetail(Long id, Long userId);
    void addMemo(MemoRequest request, Long userId);
    void updateMemo(Long id, MemoRequest request, Long userId);
    void deleteMemo(Long id, Long userId);
    void toggleMemoStatus(Long id, Long userId);
    StatisticsVO getStatistics(Long userId);
}
