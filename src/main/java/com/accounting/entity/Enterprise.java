package com.accounting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 企业信息实体类
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
@Data
@TableName("enterprise")
public class Enterprise {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 企业名称
     */
    private String name;
    
    /**
     * 注册资本
     */
    @TableField("registration_capital")
    private BigDecimal registrationCapital;
    
    /**
     * 企业地址
     */
    private String address;
}