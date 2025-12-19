package com.accounting.mapper;

import com.accounting.entity.SalesOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 销售订单Mapper接口
 */
@Mapper
public interface SalesOrderMapper extends BaseMapper<SalesOrder> {

    /**
     * 根据订单编号查询
     */
    SalesOrder selectByOrderNumber(String orderNumber, Long companyId);
}
