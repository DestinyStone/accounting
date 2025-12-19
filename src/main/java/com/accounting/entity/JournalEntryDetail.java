package com.accounting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 凭证分录明细实体类
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
@Data
@TableName("journal_entry_detail")
public class JournalEntryDetail {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 凭证分录ID
     */
    private Long entryId;
    
    /**
     * 会计科目ID
     */
    private Long subjectId;
    
    /**
     * 借方金额
     */
    private BigDecimal debitAmount;
    
    /**
     * 贷方金额
     */
    private BigDecimal creditAmount;
    
    /**
     * 备注
     */
    private String remark;
}