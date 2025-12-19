package com.accounting.mapper;

import com.accounting.entity.Enterprise;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 企业信息Mapper接口
 */
public interface EnterpriseMapper extends BaseMapper<Enterprise> {

    // BaseMapper已经包含了基本的CRUD操作
    // 可以根据需要添加自定义查询方法
}