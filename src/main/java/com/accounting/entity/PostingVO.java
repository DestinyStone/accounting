package com.accounting.entity;

import lombok.Data;
import java.util.Date;

/**
 * 过账记录视图对象
 * 包含业务来源信息
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
@Data
public class PostingVO {
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 凭证分录ID
     */
    private Long entryId;

    /**
     * 过账日期时间
     */
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
     * 业务来源类型
     */
    private String businessType;
    
    /**
     * 业务来源ID
     */
    private Long businessId;
    
    /**
     * 业务来源信息（如：采购订单编号、销售订单编号、员工姓名等）
     */
    private String businessSourceInfo;
}


