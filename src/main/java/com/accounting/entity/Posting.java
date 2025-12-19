package com.accounting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 过账实体类
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
@Data
@TableName("posting")
public class Posting {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 凭证分录ID
     */
    private Long entryId;

    /**
     * 过账日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date postingDate;
    
    /**
     * 过账用户ID
     */
    private String postingUserId;
}
