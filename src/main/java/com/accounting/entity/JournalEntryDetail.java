package com.accounting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("journal_entry_detail")
public class JournalEntryDetail {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long entryId;
    private Long subjectId;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private String remark;
}