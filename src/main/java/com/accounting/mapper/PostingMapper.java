package com.accounting.mapper;

import com.accounting.entity.Posting;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostingMapper extends BaseMapper<Posting> {
    Posting selectByEntryId(Long entryId);
}