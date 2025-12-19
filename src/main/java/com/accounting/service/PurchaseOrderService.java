package com.accounting.service;

import com.accounting.entity.PurchaseOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;

/**
 * 采购订单服务接口
 */
public interface PurchaseOrderService extends IService<PurchaseOrder> {

    /**
     * 分页查询采购订单列表
     * @param page 分页参数
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<PurchaseOrder> selectPage(Page<PurchaseOrder> page, Map<String, Object> params);

    /**
     * 根据ID查询采购订单信息
     * @param id 订单ID
     * @return 采购订单信息
     */
    PurchaseOrder selectById(Long id);

    /**
     * 删除采购订单
     * @param id 订单ID
     * @return 是否成功
     */
    boolean delete(Long id);

    /**
     * 批量删除采购订单
     * @param ids 订单ID列表
     * @return 是否成功
     */
    boolean deleteBatch(List<Long> ids);

    /**
     * 提交采购订单
     * @param id 订单ID
     * @return 是否成功
     */
    boolean submit(Long id);

    /**
     * 审核采购订单
     * @param id 订单ID
     * @return 是否成功
     */
    boolean approve(Long id);

    /**
     * 取消采购订单
     * @param id 订单ID
     * @return 是否成功
     */
    boolean cancel(Long id);
    /**
     * 生成采购订单编号
     * @param companyId 公司ID
     * @return 订单编号
     */
    String generateOrderNumber(Long companyId);

    /**
     * 根据订单编号查询订单
     * @param orderNumber 订单编号
     * @return 订单信息
     */
    PurchaseOrder selectByOrderNumber(String orderNumber);
}
