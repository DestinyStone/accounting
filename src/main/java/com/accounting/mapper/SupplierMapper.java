package com.accounting.mapper;

import com.accounting.entity.Supplier;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 供应商Mapper接口
 */
@Mapper
public interface SupplierMapper extends BaseMapper<Supplier> {
}
