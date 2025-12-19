package com.accounting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@TableName("journal_entry")
public class JournalEntry {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String voucherNo;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date voucherDate;
    private String summary;
    private BigDecimal totalDebit;
    private BigDecimal totalCredit;
    private Integer status;
    private Date createTime;

    @TableField(exist = false)
    private List<JournalEntryDetail> details;
}
