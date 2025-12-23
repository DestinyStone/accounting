package com.accounting.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 过账详情视图对象
 * 包含过账信息、凭证信息、凭证明细、业务来源信息等
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
@Data
public class PostingDetailVO {
    /**
     * 过账记录基本信息
     */
    private Posting posting;
    
    /**
     * 凭证信息
     */
    private JournalEntry journalEntry;
    
    /**
     * 凭证明细列表（包含科目信息）
     */
    private List<Map<String, Object>> entryDetails;
    
    /**
     * 业务来源信息
     */
    private String businessSourceInfo;
}


