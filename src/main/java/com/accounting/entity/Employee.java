package com.accounting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 员工实体类
 */
@Data
public class Employee {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String employeeCode;
    private String employeeName;
    private String phone;
    private Integer status; // 0:禁用 1:启用
}
