package com.accounting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("posting")
public class Posting {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long entryId;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date postingDate;
    private String postingUserId;
}
