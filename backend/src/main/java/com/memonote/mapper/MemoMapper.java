package com.memonote.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.memonote.entity.Memo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MemoMapper extends BaseMapper<Memo> {
    
    @Select("SELECT m.*, c.name as category_name, c.color as category_color " +
            "FROM memo_note m " +
            "LEFT JOIN memo_category c ON m.category_id = c.id " +
            "WHERE m.user_id = #{userId} AND m.deleted = 0 " +
            "ORDER BY m.create_time DESC")
    IPage<Memo> selectPageWithCategory(Page<Memo> page, @Param("userId") Long userId);
    
    @Select("SELECT m.*, c.name as category_name, c.color as category_color " +
            "FROM memo_note m " +
            "LEFT JOIN memo_category c ON m.category_id = c.id " +
            "WHERE m.user_id = #{userId} AND m.deleted = 0 " +
            "AND (m.title LIKE CONCAT('%', #{keyword}, '%') OR m.content LIKE CONCAT('%', #{keyword}, '%')) " +
            "ORDER BY m.create_time DESC")
    IPage<Memo> searchByKeyword(Page<Memo> page, @Param("userId") Long userId, @Param("keyword") String keyword);
}
