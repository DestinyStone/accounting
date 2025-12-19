package com.accounting.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.util.Date;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 员工费用报销实体类
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
@Data
@TableName("employee_expense")
public class EmployeeExpense {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 费用编号
     */
    private String expenseNo;

    /**
     * 员工ID
     */
    private Long employeeId;

    /**
     * 费用金额
     */
    private BigDecimal amount;

    /**
     * 费用描述
     */
    private String remark;

    /**
     * 状态：0-待审批，1-已批准，2-已报销
     */
    private Integer status;

    /**
     * 费用发生日期
     */
    private Date expenseDate;
}
