package com.accounting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 企业信息实体类
 */
@Data
@TableName("enterprise")
public class Enterprise {

    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;  // 企业名称
    
    @TableField("registration_capital")
    private BigDecimal registrationCapital;  // 注册资本
    
    private String address;  // 企业地址
}