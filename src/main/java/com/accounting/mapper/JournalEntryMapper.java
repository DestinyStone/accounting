package com.accounting.mapper;

import com.accounting.entity.JournalEntry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 凭证分录Mapper接口
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
@Mapper
public interface JournalEntryMapper extends BaseMapper<JournalEntry> {
    /**
     * 根据条件查询凭证分录（包含明细）
     * 
     * @param params 查询参数
     * @return 凭证分录列表
     */
    List<JournalEntry> selectWithDetails(@Param("params") Map<String, Object> params);
    
    /**
     * 根据ID查询凭证分录（包含明细）
     * 
     * @param id 凭证分录ID
     * @return 凭证分录对象
     */
    JournalEntry selectByIdWithDetails(Long id);
    
    /**
     * 更新凭证分录状态
     * 
     * @param id 凭证分录ID
     * @param status 状态（0-草稿，1-已审核，2-已过账）
     */
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    /**
     * 根据日期获取最大凭证号
     * 
     * @param date 日期字符串（格式：yyyy-MM-dd）
     * @return 最大凭证号
     */
    String getMaxVoucherNoByDate(@Param("date") String date);
}