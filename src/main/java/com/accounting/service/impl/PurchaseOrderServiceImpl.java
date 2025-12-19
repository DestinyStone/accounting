package com.accounting.service.impl;

import com.accounting.entity.PurchaseOrder;
import com.accounting.mapper.PurchaseOrderMapper;
import com.accounting.service.PurchaseOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 采购订单服务实现类
 */
@Service
public class PurchaseOrderServiceImpl extends ServiceImpl<PurchaseOrderMapper, PurchaseOrder> implements PurchaseOrderService {

    @Override
    public IPage<PurchaseOrder> selectPage(Page<PurchaseOrder> page, Map<String, Object> params) {
        QueryWrapper<PurchaseOrder> wrapper = new QueryWrapper<>();

        // 获取查询参数
        String orderNumber = params.get("orderNumber") != null ? params.get("orderNumber").toString() : null;
        if (orderNumber != null && !orderNumber.isEmpty()) {
            wrapper.like("order_number", orderNumber);
        }

        // 按订单日期倒序排序
        wrapper.orderByDesc("order_date");

        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public PurchaseOrder selectById(Long id) {
        return baseMapper.selectById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delete(Long id) {
        // 检查订单状态
        PurchaseOrder order = baseMapper.selectById(id);
        if (order != null) {
            // 只有草稿状态可以删除
            if (order.getStatus() != 0) {
                throw new RuntimeException("只有草稿状态的订单可以删除");
            }
        }

        // 删除订单
        return baseMapper.deleteById(id) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            // 检查每个订单的状态
            PurchaseOrder order = baseMapper.selectById(id);
            if (order != null) {
                if (order.getStatus() != 0) {
                    throw new RuntimeException("只有草稿状态的订单可以删除");
                }
            }
        }

        // 批量删除订单
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean submit(Long id) {
        // 获取订单信息
        PurchaseOrder order = baseMapper.selectById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 检查订单状态
        if (order.getStatus() != 0) {
            throw new RuntimeException("只有草稿状态的订单可以提交");
        }

        // 更新订单状态为已提交
        order.setStatus(1);
        return baseMapper.updateById(order) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean approve(Long id) {
        // 获取订单信息
        PurchaseOrder order = baseMapper.selectById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 检查订单状态
        if (order.getStatus() != 1) {
            throw new RuntimeException("只有已提交状态的订单可以审核");
        }

        // 更新订单状态为已审核
        order.setStatus(2);
        return baseMapper.updateById(order) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean cancel(Long id) {
        // 获取订单信息
        PurchaseOrder order = baseMapper.selectById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 检查订单状态，只有已提交和已审核的订单可以取消
        if (order.getStatus() != 1 && order.getStatus() != 2) {
            throw new RuntimeException("只有已提交或已审核状态的订单可以取消");
        }


        // 更新订单状态为已取消
        order.setStatus(4);

        return baseMapper.updateById(order) > 0;
    }

    @Override
    public String generateOrderNumber(Long companyId) {
        // 格式：PO + 年月日 + 4位序号
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "PO" + dateStr;

        // 查询当天最大的订单号
        QueryWrapper<PurchaseOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("company_id", companyId)
               .likeRight("order_number", prefix)
               .orderByDesc("order_number")
               .last("LIMIT 1");

        PurchaseOrder lastOrder = baseMapper.selectOne(wrapper);

        // 如果没有订单，则从0001开始
        if (lastOrder == null || lastOrder.getOrderNumber() == null) {
            return prefix + "0001";
        }

        // 提取数字部分并加1
        String lastNumber = lastOrder.getOrderNumber().substring(12);
        int num = Integer.parseInt(lastNumber) + 1;

        // 格式化为4位数字
        return prefix + String.format("%04d", num);
    }

    @Override
    public PurchaseOrder selectByOrderNumber(String orderNumber) {
        QueryWrapper<PurchaseOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_number", orderNumber);
        return baseMapper.selectOne(wrapper);
    }
}
