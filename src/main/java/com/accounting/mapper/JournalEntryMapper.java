package com.accounting.mapper;

import com.accounting.entity.JournalEntry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface JournalEntryMapper extends BaseMapper<JournalEntry> {
    List<JournalEntry> selectWithDetails(@Param("params") Map<String, Object> params);
    JournalEntry selectByIdWithDetails(Long id);
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);
    String getMaxVoucherNoByDate(@Param("date") String date);
}