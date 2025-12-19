package com.accounting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 供应商实体类
 */
@Data
@TableName("supplier")
public class Supplier {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 供应商编码
     */
    private String supplierCode;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 状态：0-禁用 1-启用
     */
    private Integer status;
}
