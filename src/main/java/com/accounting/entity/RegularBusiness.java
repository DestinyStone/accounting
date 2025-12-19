package com.accounting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("regular_business")
public class RegularBusiness {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String businessType;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date businessDate;
    private BigDecimal amount;
    private Integer status;
    private String remark;
}
