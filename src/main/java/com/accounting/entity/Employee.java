package com.accounting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 员工实体类
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
@Data
@TableName("employee")
public class Employee {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 员工编码
     */
    private String employeeCode;
    
    /**
     * 员工姓名
     */
    private String employeeName;
    
    /**
     * 电话
     */
    private String phone;
    
    /**
     * 状态（0:离职 1:在职）
     */
    private Integer status;
    
    /**
     * 基本工资 / 员工薪资
     */
    private java.math.BigDecimal baseSalary;
    
    /**
     * 入职日期（yyyy-MM-dd）
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date hireDate;

    /**
     * 公司ID
     */
    private Long companyId;
    
    /**
     * 部门
     */
    private String department;
}
