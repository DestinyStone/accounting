package com.accounting.mapper;

import com.accounting.entity.PurchaseOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采购订单Mapper接口
 */
@Mapper
public interface PurchaseOrderMapper extends BaseMapper<PurchaseOrder> {
}
