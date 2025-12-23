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
     * 费用单号
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
     * 费用类型（薪资发放、费用报销、员工补贴等）
     */
    private String expenseType;

    /**
     * 费用说明
     */
    private String remark;

    /**
     * 状态：0-未支付，1-已支付
     */
    private Integer status;

    /**
     * 费用发生日期
     */
    private Date expenseDate;
    
    /**
     * 关联的凭证ID（报销后自动生成）
     */
    private Long journalEntryId;
    
    /**
     * 费用类型科目ID（管理费用、销售费用等）
     */
    private Long expenseSubjectId;
    
    /**
     * 付款账户科目ID（银行存款等）
     */
    private Long paymentAccountId;
}
