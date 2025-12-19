package com.accounting.mapper;

import com.accounting.entity.JournalEntryDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 凭证分录明细Mapper接口
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
@Mapper
public interface JournalEntryDetailMapper extends BaseMapper<JournalEntryDetail> {
    /**
     * 根据凭证分录ID查询明细列表
     * 
     * @param entryId 凭证分录ID
     * @return 明细列表
     */
    List<JournalEntryDetail> selectByEntryId(Long entryId);
    
    /**
     * 根据凭证分录ID删除明细
     * 
     * @param entryId 凭证分录ID
     */
    void deleteByEntryId(Long entryId);
    
    /**
     * 批量插入明细
     * 
     * @param details 明细列表
     */
    void batchInsert(@Param("details") List<JournalEntryDetail> details);
}