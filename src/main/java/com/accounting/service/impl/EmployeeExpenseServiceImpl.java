package com.accounting.service.impl;

import com.accounting.entity.Employee;
import com.accounting.entity.EmployeeExpense;
import com.accounting.mapper.EmployeeExpenseMapper;
import com.accounting.mapper.EmployeeMapper;
import com.accounting.service.EmployeeExpenseService;
import com.accounting.service.VoucherGenerationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 员工费用服务实现类
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
@Service
public class EmployeeExpenseServiceImpl implements EmployeeExpenseService {

    @Autowired
    private EmployeeExpenseMapper employeeExpenseMapper;
    
    @Autowired
    private VoucherGenerationService voucherGenerationService;
    
    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public IPage<EmployeeExpense> page(Integer pageNum, Integer pageSize, String expenseType, Integer status) {
        QueryWrapper<EmployeeExpense> wrapper = new QueryWrapper<>();
        if (expenseType != null && !expenseType.isEmpty()) {
            wrapper.eq("expense_type", expenseType);
        }
        if (status != null) {
            wrapper.eq("status", status);
        }
        wrapper.orderByDesc("id");
        return employeeExpenseMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public EmployeeExpense getById(Long id) {
        return employeeExpenseMapper.selectById(id);
    }

    @Override
    public boolean insert(EmployeeExpense expense) {
        // 自动生成费用单号
        if (expense.getExpenseNo() == null || expense.getExpenseNo().trim().isEmpty()) {
            expense.setExpenseNo(generateExpenseNo());
        }
        // 默认状态未支付
        if (expense.getStatus() == null) {
            expense.setStatus(0);
        }
        // 费用日期默认当前时间
        if (expense.getExpenseDate() == null) {
            expense.setExpenseDate(new Date());
        }
        return employeeExpenseMapper.insert(expense) > 0;
    }

    /**
     * 生成费用单号：EE + yyyyMMddHHmmss + 3位随机数
     */
    private String generateExpenseNo() {
        String timePart = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String randomPart = String.valueOf((int) (Math.random() * 900) + 100);
        return "EE" + timePart + randomPart;
    }

    @Override
    public boolean update(EmployeeExpense expense) {
        // 加载原记录，用于判断状态变更
        EmployeeExpense old = employeeExpenseMapper.selectById(expense.getId());
        if (old == null) {
            throw new RuntimeException("费用记录不存在");
        }

        // 已支付的费用不允许回退为未支付
        if (old.getStatus() != null && old.getStatus() == 1
                && expense.getStatus() != null && expense.getStatus() == 0) {
            throw new RuntimeException("已支付的费用不能修改为未支付");
        }

        // 当状态从未支付变为已支付时，自动生成凭证（以及必要的税务处理）
        if ((old.getStatus() == null || old.getStatus() == 0)
                && expense.getStatus() != null && expense.getStatus() == 1
                && old.getJournalEntryId() == null) {

            // 获取员工信息
            Employee employee = null;
            String employeeName = "未知员工";
            if (old.getEmployeeId() != null) {
                employee = employeeMapper.selectById(old.getEmployeeId());
                if (employee != null) {
                    employeeName = employee.getEmployeeName();
                }
            }

            Long journalEntryId;
            // 如果费用类型为“薪资发放”，走工资发放逻辑（自动产生税务记录）
            if ("薪资发放".equals(expense.getExpenseType())) {
                Long paymentAccountId = old.getPaymentAccountId();
                journalEntryId = voucherGenerationService.generateSalaryVoucher(
                        old.getEmployeeId(),
                        employeeName,
                        old.getAmount(),
                        paymentAccountId
                )[0];
            } else {
                // 其它费用类型走普通费用报销逻辑（仅生成凭证）
                journalEntryId = voucherGenerationService.generateExpenseVoucher(
                        old.getId(),
                        old.getAmount(),
                        old.getExpenseSubjectId(),
                        old.getPaymentAccountId(),
                        employeeName
                );
            }

            expense.setJournalEntryId(journalEntryId);
        }

        return employeeExpenseMapper.updateById(expense) > 0;
    }

    @Override
    public boolean delete(Long id) {
        return employeeExpenseMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional
    public boolean deleteBatch(List<Long> ids) {
        return employeeExpenseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approve(Long id) {
        // 查询费用信息
        EmployeeExpense expense = employeeExpenseMapper.selectById(id);
        if (expense == null) {
            throw new RuntimeException("费用记录不存在");
        }
        
        // 检查状态：已支付不可重复操作
        if (expense.getStatus() != null && expense.getStatus() == 1) {
            throw new RuntimeException("该费用已支付，不能重复操作");
        }
        
        // 检查是否已生成凭证
        if (expense.getJournalEntryId() != null) {
            throw new RuntimeException("该费用已生成凭证，不能重复支付");
        }
        
        // 获取员工信息
        Employee employee = null;
        String employeeName = "未知员工";
        if (expense.getEmployeeId() != null) {
            employee = employeeMapper.selectById(expense.getEmployeeId());
            if (employee != null) {
                employeeName = employee.getEmployeeName();
            }
        }
        
        Long journalEntryId;
        // 如果费用类型为“薪资发放”，走工资发放逻辑（自动产生税务记录）
        if ("薪资发放".equals(expense.getExpenseType())) {
            Long paymentAccountId = expense.getPaymentAccountId();
            journalEntryId = voucherGenerationService.generateSalaryVoucher(
                    expense.getEmployeeId(),
                    employeeName,
                    expense.getAmount(),
                    paymentAccountId
            )[0];
        } else {
            // 其它费用类型走普通费用报销逻辑（仅生成凭证）
            journalEntryId = voucherGenerationService.generateExpenseVoucher(
                    expense.getId(),
                    expense.getAmount(),
                    expense.getExpenseSubjectId(),
                    expense.getPaymentAccountId(),
                    employeeName
            );
        }

        // 更新费用状态为已支付，并关联凭证ID
        expense.setStatus(1); // 1表示已支付
        expense.setJournalEntryId(journalEntryId);
        return employeeExpenseMapper.updateById(expense) > 0;
    }

    @Override
    @Transactional
    public boolean approveBatch(List<Long> ids) {
        int count = 0;
        for (Long id : ids) {
            if (approve(id)) {
                count++;
            }
        }
        return count > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean reimburse(Long id, EmployeeExpense expense) {
        // 为兼容旧接口，这里直接调用 approve 逻辑，将费用视为支付完成
        return approve(id);
    }
}