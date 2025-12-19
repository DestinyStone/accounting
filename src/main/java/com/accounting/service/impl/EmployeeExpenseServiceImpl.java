package com.accounting.service.impl;

import com.accounting.entity.EmployeeExpense;
import com.accounting.mapper.EmployeeExpenseMapper;
import com.accounting.service.EmployeeExpenseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 员工费用服务实现类
 */
@Service
public class EmployeeExpenseServiceImpl implements EmployeeExpenseService {

    @Autowired
    private EmployeeExpenseMapper employeeExpenseMapper;

    @Override
    public IPage<EmployeeExpense> page(Integer pageNum, Integer pageSize, String expenseName, Integer status) {
        QueryWrapper<EmployeeExpense> wrapper = new QueryWrapper<>();
        if (expenseName != null && !expenseName.isEmpty()) {
            wrapper.like("expense_name", expenseName);
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
        return employeeExpenseMapper.insert(expense) > 0;
    }

    @Override
    public boolean update(EmployeeExpense expense) {
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
    public boolean approve(Long id) {
        EmployeeExpense expense = new EmployeeExpense();
        expense.setId(id);
        expense.setStatus(1); // 1表示已报销
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
    public boolean reimburse(Long id, EmployeeExpense expense) {
        expense.setId(id);
        expense.setStatus(1); // 1表示已报销
        return employeeExpenseMapper.updateById(expense) > 0;
    }
}