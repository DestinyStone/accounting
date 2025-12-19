package com.accounting.mapper;

import com.accounting.entity.JournalEntryDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JournalEntryDetailMapper extends BaseMapper<JournalEntryDetail> {
    List<JournalEntryDetail> selectByEntryId(Long entryId);
    void deleteByEntryId(Long entryId);
    void batchInsert(@Param("details") List<JournalEntryDetail> details);
}