package com.accounting.service;

import com.accounting.entity.EmployeeExpense;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;

/**
 * 员工费用服务接口
 */
public interface EmployeeExpenseService {

    /**
     * 分页查询费用列表
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param expenseType 费用类型（薪资发放、费用报销、员工补贴等）
     * @param status 状态（0-未支付，1-已支付）
     */
    IPage<EmployeeExpense> page(Integer pageNum, Integer pageSize, String expenseType, Integer status);

    /**
     * 根据ID查询费用
     */
    EmployeeExpense getById(Long id);

    /**
     * 新增费用
     */
    boolean insert(EmployeeExpense expense);

    /**
     * 更新费用
     */
    boolean update(EmployeeExpense expense);

    /**
     * 删除费用
     */
    boolean delete(Long id);

    /**
     * 批量删除费用
     */
    boolean deleteBatch(List<Long> ids);

    /**
     * 报销费用（审批通过）
     */
    boolean approve(Long id);

    /**
     * 批量报销费用
     */
    boolean approveBatch(List<Long> ids);

    /**
     * 财务报销处理
     */
    boolean reimburse(Long id, EmployeeExpense expense);
}