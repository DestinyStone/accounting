package com.accounting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 采购订单实体类
 *
 * @author Accounting Platform
 * @version 1.0.0
 */
@Data
@TableName("purchase_order")
public class PurchaseOrder {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订单编号
     */
    private String orderNumber;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 订单日期
     */
    private LocalDateTime orderDate;

    /**
     * 订单金额
     */
    private BigDecimal totalAmount;

    /**
     * 订单状态：0-待审核 1-已审核 2-已过账 3-已取消
     */
    private Integer status;
    
    /**
     * 税额（进项税，增值税）
     */
    private BigDecimal taxAmount;
    
    /**
     * 税率（如13%）
     */
    private BigDecimal taxRate;
    
    /**
     * 不含税金额
     */
    private BigDecimal amountWithoutTax;

    /**
     * 备注
     */
    private String remark;

    /**
     * 关联的凭证ID（审核后自动生成）
     */
    private Long journalEntryId;

    /**
     * 公司ID
     */
    private Long companyId;

    /**
     * 付款方式（1-现金，2-银行转账，3-应付账款）
     */
    private Integer paymentMethod;

    /**
     * 付款账户科目ID（银行存款等）
     */
    private Long paymentAccountId;
}
