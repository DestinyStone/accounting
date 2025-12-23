package com.accounting.service.impl;

import com.accounting.entity.PurchaseOrder;
import com.accounting.entity.Supplier;
import com.accounting.mapper.PurchaseOrderMapper;
import com.accounting.mapper.SupplierMapper;
import com.accounting.service.PurchaseOrderService;
import com.accounting.service.VoucherGenerationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 采购订单服务实现类
 *
 * @author Accounting Platform
 * @version 1.0.0
 */
@Service
public class PurchaseOrderServiceImpl extends ServiceImpl<PurchaseOrderMapper, PurchaseOrder> implements PurchaseOrderService {

    @Autowired
    private VoucherGenerationService voucherGenerationService;
    
    @Autowired
    private SupplierMapper supplierMapper;
    
    @Autowired
    private com.accounting.service.AccountingSubjectService accountingSubjectService;

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
            // 只有待审核状态可以删除
            if (order.getStatus() != 0) {
                throw new RuntimeException("只有待审核状态的订单可以删除");
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

        // 检查订单状态（现在0是待审核，不需要提交步骤，直接审核）
        // 这个方法保留用于兼容，但实际状态已经是待审核
        if (order.getStatus() != 0) {
            throw new RuntimeException("订单状态不正确，无法提交");
        }

        // 订单创建时就是待审核状态，提交操作实际上不做任何改变
        // 或者可以理解为提交后进入待审核状态
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean approve(Long id) {
        // 获取订单信息
        PurchaseOrder order = baseMapper.selectById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 检查订单状态：0-待审核
        if (order.getStatus() != 0) {
            throw new RuntimeException("只有待审核状态的订单可以审核");
        }

        // 检查是否已生成凭证
        if (order.getJournalEntryId() != null) {
            throw new RuntimeException("该订单已生成凭证，不能重复审核");
        }

        // 获取供应商信息
        Supplier supplier = null;
        String supplierName = "未知供应商";
        if (order.getSupplierId() != null) {
            supplier = supplierMapper.selectById(order.getSupplierId());
            if (supplier != null) {
                supplierName = supplier.getSupplierName();
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

        // 优先使用绑定的科目
        com.accounting.entity.AccountingSubject boundSubject = accountingSubjectService.getByBusinessType("PURCHASE");
        Long expenseSubjectId = null;
        if (boundSubject != null) {
            expenseSubjectId = boundSubject.getId();
        } else if (order.getPaymentAccountId() != null) {
            expenseSubjectId = order.getPaymentAccountId();
        } else {
            // 默认使用库存商品科目（1405）
            Long defaultSubjectId = voucherGenerationService.findSubjectIdByCode("1405");
            if (defaultSubjectId == null) {
                throw new RuntimeException("未找到采购单绑定的科目，请在会计科目管理中绑定采购单科目");
            }
            expenseSubjectId = defaultSubjectId;
        }

        // 自动生成凭证和税务记录
        Long[] result = voucherGenerationService.generatePurchaseVoucher(
            order.getId(),
            order.getTotalAmount(),
            taxAmount,
            order.getPaymentMethod() != null ? order.getPaymentMethod() : 3, // 默认应付账款
            order.getPaymentAccountId(),
            expenseSubjectId,
            supplierName
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean cancel(Long id) {
        // 获取订单信息
        PurchaseOrder order = baseMapper.selectById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 检查订单状态，只有待审核和已审核的订单可以取消
        if (order.getStatus() != 0 && order.getStatus() != 1) {
            throw new RuntimeException("只有待审核或已审核状态的订单可以取消");
        }

        if (order.getJournalEntryId() != null) {
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
