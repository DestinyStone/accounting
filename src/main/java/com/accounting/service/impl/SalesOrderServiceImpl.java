package com.accounting.service.impl;

import com.accounting.entity.SalesOrder;
import com.accounting.mapper.SalesOrderMapper;
import com.accounting.service.SalesOrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 销售订单服务实现类
 */
@Service
public class SalesOrderServiceImpl extends ServiceImpl<SalesOrderMapper, SalesOrder> implements SalesOrderService {

    @Autowired
    private SalesOrderMapper salesOrderMapper;

    @Override
    public Page<SalesOrder> page(Integer pageNum, Integer pageSize, Long companyId, String orderNumber, Long customerId, Integer status, String startDate, String endDate) {
        QueryWrapper<SalesOrder> wrapper = new QueryWrapper<>();
        if (orderNumber != null && !orderNumber.isEmpty()) {
            wrapper.like("order_number", orderNumber);
        }
        return baseMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public Map<String, Object> getById(Long id) {
        Map<String, Object> result = new HashMap<>();
        // 查询订单信息
        SalesOrder order = baseMapper.selectById(id);
        if (order == null) {
            throw new RuntimeException("销售订单不存在");
        }
        result.put("order", order);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(SalesOrder order) {
        // 检查订单编号是否存在
        LambdaQueryWrapper<SalesOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SalesOrder::getOrderNumber, order.getOrderNumber());
        SalesOrder existing = salesOrderMapper.selectOne(wrapper);
        if (existing != null) {
            throw new RuntimeException("订单编号已存在");
        }
        // 插入订单信息
        order.setStatus(0); // 草稿状态
        order.setOrderDate(new Date());
        baseMapper.insert(order);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(SalesOrder order) {
        // 查询原订单
        SalesOrder oldOrder = baseMapper.selectById(order.getId());
        if (oldOrder == null) {
            throw new RuntimeException("销售订单不存在");
        }
        // 只有草稿状态的订单可以修改
        if (oldOrder.getStatus() != 0) {
            throw new RuntimeException("只有草稿状态的订单可以修改");
        }
        // 更新订单信息
        baseMapper.updateById(order);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        // 查询订单
        SalesOrder order = baseMapper.selectById(id);
        if (order == null) {
            throw new RuntimeException("销售订单不存在");
        }
        // 只有草稿状态的订单可以删除
        if (order.getStatus() != 0) {
            throw new RuntimeException("只有草稿状态的订单可以删除");
        }
        // 删除订单
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            SalesOrder order = baseMapper.selectById(id);
            if (order != null && order.getStatus() != 0) {
                throw new RuntimeException("包含非草稿状态的订单，无法批量删除");
            }
        }
        // 删除订单
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submit(Long id, Long userId) {
        // 查询订单
        SalesOrder order = baseMapper.selectById(id);
        if (order == null) {
            throw new RuntimeException("销售订单不存在");
        }
        // 检查状态
        if (order.getStatus() != 0) {
            throw new RuntimeException("订单状态不正确，无法提交");
        }
        // 更新订单状态
        order.setStatus(1); // 已提交
        return baseMapper.updateById(order) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approve(Long id, Long userId) {
        // 查询订单
        SalesOrder order = baseMapper.selectById(id);
        if (order == null) {
            throw new RuntimeException("销售订单不存在");
        }
        // 检查状态
        if (order.getStatus() != 1) {
            throw new RuntimeException("订单状态不正确，无法审核");
        }
        // 更新订单状态
        order.setStatus(2); // 已审核
        return baseMapper.updateById(order) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancel(Long id, Long userId) {
        // 查询订单
        SalesOrder order = baseMapper.selectById(id);
        if (order == null) {
            throw new RuntimeException("销售订单不存在");
        }
        // 检查状态
        if (order.getStatus() != 1 && order.getStatus() != 2) {
            throw new RuntimeException("订单状态不正确，无法取消");
        }
        // 更新订单状态
        order.setStatus(4); // 已取消
        return baseMapper.updateById(order) > 0;
    }

}
