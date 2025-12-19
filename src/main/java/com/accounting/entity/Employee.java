package com.accounting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

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
     * 状态（0:禁用 1:启用）
     */
    private Integer status;
}
