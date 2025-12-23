package com.accounting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 对账实体类
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
@Data
@TableName("reconciliation")
public class Reconciliation {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 账户ID（会计科目ID）
     */
    private Long accountId;
    
    /**
     * 对账日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date reconciliationDate;
    
    /**
     * 账面余额（系统根据已过账凭证计算的余额）
     */
    private BigDecimal bookBalance;
    
    /**
     * 对账单余额（人工录入的外部对账单余额，如银行对账单）
     */
    private BigDecimal statementBalance;
    
    /**
     * 差额 = 对账单余额 - 账面余额
     */
    private BigDecimal difference;
    
    /**
     * 状态（0-未对账，1-已对账）
     */
    private Integer status;
    
    /**
     * 对账说明
     */
    private String remark;
    
    /**
     * 对账人ID
     */
    private Long userId;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;
}
