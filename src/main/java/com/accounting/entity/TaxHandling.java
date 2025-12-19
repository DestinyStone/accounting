package com.accounting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 税务处理实体类
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
@Data
@TableName("tax_handling")
public class TaxHandling {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 税种类型
     */
    private String taxType;
    
    /**
     * 纳税期间
     */
    private String taxPeriod;
    
    /**
     * 税额
     */
    private BigDecimal taxAmount;

    /**
     * 缴税日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paymentDate;
    
    /**
     * 状态（0-未缴，1-已缴）
     */
    private Integer status;
}
