package com.accounting.service.impl;

import com.accounting.entity.Customer;
import com.accounting.entity.SalesOrder;
import com.accounting.mapper.CustomerMapper;
import com.accounting.mapper.SalesOrderMapper;
import com.accounting.service.SalesOrderService;
import com.accounting.service.VoucherGenerationService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 销售订单服务实现类
 *
 * @author Accounting Platform
 * @version 1.0.0
 */
@Service
public class SalesOrderServiceImpl extends ServiceImpl<SalesOrderMapper, SalesOrder> implements SalesOrderService {

    @Autowired
    private SalesOrderMapper salesOrderMapper;

    @Autowired
    private VoucherGenerationService voucherGenerationService;
    
    @Autowired
    private CustomerMapper customerMapper;
    
    @Autowired
    private com.accounting.service.AccountingSubjectService accountingSubjectService;

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
        order.setStatus(0); // 待审核状态
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
        // 只有待审核状态的订单可以修改
        if (oldOrder.getStatus() != 0) {
            throw new RuntimeException("只有待审核状态的订单可以修改");
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
        // 只有待审核状态的订单可以删除
        if (order.getStatus() != 0) {
            throw new RuntimeException("只有待审核状态的订单可以删除");
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
        // 检查状态（现在0是待审核，不需要提交步骤）
        if (order.getStatus() != 0) {
            throw new RuntimeException("订单状态不正确，无法提交");
        }
        // 订单创建时就是待审核状态，提交操作实际上不做任何改变
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approve(Long id, Long userId) {
        // 查询订单
        SalesOrder order = baseMapper.selectById(id);
        if (order == null) {
            throw new RuntimeException("销售订单不存在");
        }
        // 检查状态：0-待审核
        if (order.getStatus() != 0) {
            throw new RuntimeException("只有待审核状态的订单可以审核");
        }

        // 检查是否已生成凭证
        if (order.getJournalEntryId() != null) {
            throw new RuntimeException("该订单已生成凭证，不能重复审核");
        }

        // 获取客户信息
        Customer customer = null;
        String customerName = "未知客户";
        if (order.getCustomerId() != null) {
            customer = customerMapper.selectById(order.getCustomerId());
            if (customer != null) {
                customerName = customer.getCustomerName();
            }
        }

        // 计算税额（如果没有设置，则根据税率计算）
        BigDecimal taxAmount = order.getTaxAmount();
        if (taxAmount == null || taxAmount.compareTo(BigDecimal.ZERO) == 0) {
            if (order.getTaxRate() != null && order.getTaxRate().compareTo(BigDecimal.ZERO) > 0) {
                // 根据税率计算税额
                BigDecimal amountWithoutTax = order.getTotalAmount().divide(
                    BigDecimal.ONE.add(order.getTaxRate().divide(new BigDecimal("100"))),
                    2,
                    BigDecimal.ROUND_HALF_UP
                );
                taxAmount = order.getTotalAmount().subtract(amountWithoutTax);
            } else {
                // 默认13%税率
                BigDecimal defaultTaxRate = new BigDecimal("13");
                BigDecimal amountWithoutTax = order.getTotalAmount().divide(
                    BigDecimal.ONE.add(defaultTaxRate.divide(new BigDecimal("100"))),
                    2,
                    BigDecimal.ROUND_HALF_UP
                );
                taxAmount = order.getTotalAmount().subtract(amountWithoutTax);
            }
        }

        // 自动生成凭证和税务记录
        Long[] result = voucherGenerationService.generateSalesVoucher(
            order.getId(),
            order.getTotalAmount(),
            taxAmount,
            order.getPaymentMethod() != null ? order.getPaymentMethod() : 3, // 默认应收账款
            order.getPaymentAccountId(),
            customerName
        );

        // 更新订单状态为已审核，并关联凭证ID
        order.setStatus(1); // 1-已审核
        order.setJournalEntryId(result[0]);
        if (order.getTaxAmount() == null) {
            order.setTaxAmount(taxAmount);
        }
        if (order.getAmountWithoutTax() == null) {
            order.setAmountWithoutTax(order.getTotalAmount().subtract(taxAmount));
        }
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
        // 检查状态：只有待审核和已审核的订单可以取消
        if (order.getStatus() != 0 && order.getStatus() != 1) {
            throw new RuntimeException("只有待审核或已审核状态的订单可以取消");
        }
        // 更新订单状态
        order.setStatus(4); // 已取消
        return baseMapper.updateById(order) > 0;
    }

}
