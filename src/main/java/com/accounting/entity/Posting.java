package com.accounting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 过账实体类
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
@Data
@TableName("posting")
public class Posting {
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
     * 过账日期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date postingDate;
    
    /**
     * 过账用户ID
     */
    private Long postingUserId;
    
    /**
     * 过账人姓名
     */
    private String postingUserName;
    
    /**
     * 过账说明
     */
    private String remark;

    /**
     * 业务来源类型（与凭证保持一致：PURCHASE/SALES/SALARY/EXPENSE/TAX 等）
     */
    private String businessType;

    /**
     * 业务来源ID（关联的业务单据ID）
     */
    private Long businessId;
}
