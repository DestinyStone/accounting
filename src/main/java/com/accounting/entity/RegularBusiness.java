package com.accounting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 常规业务实体类
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
@Data
@TableName("regular_business")
public class RegularBusiness {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 业务日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date businessDate;
    
    /**
     * 金额
     */
    private BigDecimal amount;
    
    /**
     * 状态（0-草稿，1-已审核，2-已过账）
     */
    private Integer status;
    
    /**
     * 备注
     */
    private String remark;
}
