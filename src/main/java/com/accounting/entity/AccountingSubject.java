package com.accounting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 会计科目实体类
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
@Data
@TableName("accounting_subject")
public class AccountingSubject {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 科目编码
     */
    private String code;

    /**
     * 科目名称
     */
    private String name;

    /**
     * 科目类型(1:资产,2:负债,3:共同,4:所有者权益,5:成本,6:损益)
     */
    private Integer type;

    /**
     * 余额方向(1:借方,2:贷方)
     */
    @TableField("balance_direction")
    private Integer balanceDirection;

    /**
     * 状态(0:禁用,1:启用)
     */
    private Integer status;
    
    /**
     * 业务绑定类型（PURCHASE-采购单，SALES-销售单，EXPENSE-员工费用，TAX-税务处理）
     */
    private String businessType;
}
