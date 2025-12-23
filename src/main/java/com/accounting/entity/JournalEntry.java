package com.accounting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 凭证分录实体类
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
@Data
@TableName("journal_entry")
public class JournalEntry {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 凭证号
     */
    private String voucherNo;

    /**
     * 凭证日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date voucherDate;
    
    /**
     * 摘要
     */
    private String summary;
    
    /**
     * 借方总额
     */
    private BigDecimal totalDebit;
    
    /**
     * 贷方总额
     */
    private BigDecimal totalCredit;
    
    /**
     * 状态（0-未过账，1-已过账）
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 业务来源类型（PURCHASE-采购订单，SALES-销售订单，SALARY-工资发放，EXPENSE-费用报销，TAX-税务）
     */
    private String businessType;
    
    /**
     * 业务来源ID（关联的业务单据ID）
     */
    private Long businessId;
    
    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 凭证明细列表（非数据库字段）
     */
    @TableField(exist = false)
    private List<JournalEntryDetail> details;
}
