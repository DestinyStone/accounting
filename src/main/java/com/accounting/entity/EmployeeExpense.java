package com.accounting.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.util.Date;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 员工费用报销实体类
 */
@Data
@TableName("employee_expense")
public class EmployeeExpense {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String expenseNo; // 费用编号

    private Long employeeId; // 员工ID

    private BigDecimal amount; // 费用金额

    private String remark; // 费用描述

    private Integer status; // 状态：0-待审批，1-已批准，2-已报销

    private Date expenseDate; // 费用发生日期
}
