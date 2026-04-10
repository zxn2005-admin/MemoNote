package com.memonote.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.memonote.entity.Memo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemoMapper extends BaseMapper<Memo> {
}
