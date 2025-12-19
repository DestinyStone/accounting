package com.accounting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 采购订单实体类
 */
@Data
@TableName("purchase_order")
public class PurchaseOrder {

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
     * 订单状态：0-草稿 1-已提交 2-已审核 3-已完成 4-已取消 5-已过账
     */
    private Integer status;

    private String remark;
}
