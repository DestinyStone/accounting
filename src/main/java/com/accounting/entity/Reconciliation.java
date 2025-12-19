package com.accounting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("reconciliation")
public class Reconciliation {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String accountId;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date reconciliationDate;
    private BigDecimal startBalance;
    private BigDecimal endBalance;
    private Integer status;
}
