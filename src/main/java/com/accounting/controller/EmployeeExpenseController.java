package com.accounting.controller;

import com.accounting.common.Api;
import com.accounting.common.Result;
import com.accounting.entity.EmployeeExpense;
import com.accounting.service.EmployeeExpenseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 员工费用控制器
 */
@RestController
@RequestMapping(Api.path + "/employee-expense")
public class EmployeeExpenseController {

    @Autowired
    private EmployeeExpenseService employeeExpenseService;

    /**
     * 分页查询费用列表
     */
    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer pageNum,
                      @RequestParam(defaultValue = "10") Integer pageSize,
                      String expenseName,
                      Integer status) {
        IPage<EmployeeExpense> page = employeeExpenseService.page(pageNum, pageSize, expenseName, status);
        return Result.success(page);
    }

    /**
     * 根据ID查询费用
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        EmployeeExpense expense = employeeExpenseService.getById(id);
        return Result.success(expense);
    }

    /**
     * 新增费用
     */
    @PostMapping
    public Result add(@RequestBody EmployeeExpense expense) {
        boolean success = employeeExpenseService.insert(expense);
        if (success) {
            return Result.success("新增成功");
        } else {
            return Result.error("新增失败");
        }
    }

    /**
     * 更新费用
     */
    @PutMapping
    public Result update(@RequestBody EmployeeExpense expense) {
        boolean success = employeeExpenseService.update(expense);
        if (success) {
            return Result.success("更新成功");
        } else {
            return Result.error("更新失败");
        }
    }

    /**
     * 删除费用
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        boolean success = employeeExpenseService.delete(id);
        if (success) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }

    /**
     * 批量删除费用
     */
    @DeleteMapping("/batch")
    public Result deleteBatch(@RequestBody List<Long> ids) {
        boolean success = employeeExpenseService.deleteBatch(ids);
        if (success) {
            return Result.success("批量删除成功");
        } else {
            return Result.error("批量删除失败");
        }
    }

    /**
     * 报销费用（审批通过）
     */
    @PutMapping("/approve/{id}")
    public Result approve(@PathVariable Long id) {
        boolean success = employeeExpenseService.approve(id);
        if (success) {
            return Result.success("报销成功");
        } else {
            return Result.error("报销失败");
        }
    }

    /**
     * 批量报销费用
     */
    @PutMapping("/approveBatch")
    public Result approveBatch(@RequestBody List<Long> ids) {
        boolean success = employeeExpenseService.approveBatch(ids);
        if (success) {
            return Result.success("批量报销成功");
        } else {
            return Result.error("批量报销失败");
        }
    }

    /**
     * 财务报销处理
     */
    @PutMapping("/reimburse/{id}")
    public Result reimburse(@PathVariable Long id, @RequestBody EmployeeExpense expense) {
        boolean success = employeeExpenseService.reimburse(id, expense);
        if (success) {
            return Result.success("报销处理成功");
        } else {
            return Result.error("报销处理失败");
        }
    }
}
