package com.accounting.service;

import com.accounting.entity.JournalEntry;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 凭证分录服务接口
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
public interface JournalEntryService extends IService<JournalEntry> {
    /**
     * 保存凭证分录（包含明细）
     * 
     * @param journalEntry 凭证分录对象
     * @return 保存后的凭证分录对象
     */
    JournalEntry saveEntry(JournalEntry journalEntry);
    
    /**
     * 更新凭证分录（包含明细）
     * 
     * @param journalEntry 凭证分录对象
     * @return 更新后的凭证分录对象
     */
    JournalEntry updateEntry(JournalEntry journalEntry);
    
    /**
     * 根据ID查询凭证分录（包含明细）
     * 
     * @param id 凭证分录ID
     * @return 凭证分录对象
     */
    JournalEntry getByIdWithDetails(Long id);
    
    /**
     * 根据条件查询凭证分录（包含明细）
     * 
     * @param params 查询参数
     * @return 凭证分录列表
     */
    List<JournalEntry> queryWithDetails(Map<String, Object> params);
    
    /**
     * 删除凭证分录（包含明细）
     * 
     * @param id 凭证分录ID
     * @return 是否删除成功
     */
    boolean deleteEntry(Long id);
    
    /**
     * 生成凭证号
     * 
     * @param date 日期字符串（格式：yyyy-MM-dd）
     * @return 生成的凭证号
     */
    String generateVoucherNo(String date);
    
    /**
     * 获取凭证明细（包含科目信息）
     * 
     * @param entryId 凭证ID
     * @return 明细列表（包含科目编码和科目名称）
     */
    List<Map<String, Object>> getDetailsWithSubject(Long entryId);
}
