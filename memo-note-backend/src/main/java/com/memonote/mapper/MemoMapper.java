package com.memonote.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.memonote.entity.Memo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MemoMapper extends BaseMapper<Memo> {

    @Select("SELECT m.*, c.name as category_name, c.color as category_color " +
            "FROM memo m LEFT JOIN category c ON m.category_id = c.id " +
            "WHERE m.user_id = #{userId} " +
            "ORDER BY m.create_time DESC")
    List<Memo> selectMemoListWithCategory(@Param("userId") Long userId);

    @Select("SELECT m.*, c.name as category_name, c.color as category_color " +
            "FROM memo m LEFT JOIN category c ON m.category_id = c.id " +
            "WHERE m.user_id = #{userId} " +
            "ORDER BY m.create_time DESC")
    Page<Memo> selectMemoPageWithCategory(Page<Memo> page, @Param("userId") Long userId);
}
