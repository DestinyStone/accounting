package com.accounting.service;

import com.accounting.entity.SalesOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;
import java.util.Map;

/**
 * 销售订单服务接口
 */
public interface SalesOrderService extends IService<SalesOrder> {

    /**
     * 分页查询销售订单列表
     */
    IPage<SalesOrder> page(Integer pageNum, Integer pageSize, Long companyId, String orderNumber, Long customerId, Integer status, String startDate, String endDate);

    /**
     * 根据ID查询销售订单及明细
     */
    Map<String, Object> getById(Long id);

    /**
     * 新增销售订单
     */
    boolean insert(SalesOrder order);

    /**
     * 更新销售订单
     */
    boolean update(SalesOrder order);

    /**
     * 删除销售订单
     */
    boolean delete(Long id);

    /**
     * 批量删除销售订单
     */
    boolean deleteBatch(List<Long> ids);

    /**
     * 提交销售订单
     */
    boolean submit(Long id, Long userId);

    /**
     * 审核销售订单
     */
    boolean approve(Long id, Long userId);

    /**
     * 取消销售订单
     */
    boolean cancel(Long id, Long userId);
}
