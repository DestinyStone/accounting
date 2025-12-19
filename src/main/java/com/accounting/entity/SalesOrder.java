package com.accounting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 销售订单实体类
 */
@Data
@TableName("sales_order")
public class SalesOrder {

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
     * 客户ID
     */
    private Long customerId;

    /**
     * 订单日期
     */
    private Date orderDate;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 订单状态（0：草稿 1：已提交 2：已审核 3：已完成 4：已取消）
     */
    private Integer status;

    private String remark;
}
