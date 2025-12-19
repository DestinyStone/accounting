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
     * 账户ID
     */
    private String accountId;
    
    /**
     * 对账日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date reconciliationDate;
    
    /**
     * 期初余额
     */
    private BigDecimal startBalance;
    
    /**
     * 期末余额
     */
    private BigDecimal endBalance;
    
    /**
     * 状态（0-未对账，1-已对账）
     */
    private Integer status;
}
