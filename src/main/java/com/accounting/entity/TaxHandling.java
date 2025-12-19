package com.accounting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("tax_handling")
public class TaxHandling {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String taxType;
    private String taxPeriod;
    private BigDecimal taxAmount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paymentDate;
    private Integer status;
}
