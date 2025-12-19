package com.accounting.controller;

import com.accounting.common.Api;
import com.accounting.common.Result;
import com.accounting.entity.Employee;
import com.accounting.service.EmployeeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 员工控制器
 */
@RestController
@RequestMapping(Api.path + "/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 分页查询员工列表
     */
    @GetMapping("/page")
    public Result page(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       Long companyId,
                       String employeeCode,
                       String employeeName,
                       String department,
                       Integer status) {
        IPage<Employee> page = employeeService.page(pageNum, pageSize, companyId, employeeCode,
                                                        employeeName, department, status);
        return Result.success(page);
    }

    /**
     * 根据ID查询员工
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * 根据员工编码查询
     */
    @GetMapping("/byCode")
    public Result getByCode(String employeeCode, Long companyId) {
        Employee employee = employeeService.getByCode(employeeCode, companyId);
        return Result.success(employee);
    }

    /**
     * 根据公司ID查询启用的员工
     */
    @GetMapping("/activeByCompanyId")
    public Result getActiveByCompanyId(Long companyId) {
        List<Employee> list = employeeService.getActiveByCompanyId(companyId);
        return Result.success(list);
    }

    /**
     * 新增员工
     */
    @PostMapping
    public Result insert(@RequestBody Employee employee) {
        boolean success = employeeService.insert(employee);
        if (success) {
            return Result.success("新增成功");
        } else {
            return Result.error("新增失败");
        }
    }

    /**
     * 修改员工
     */
    @PutMapping
    public Result update(@RequestBody Employee employee) {
        boolean success = employeeService.update(employee);
        if (success) {
            return Result.success("修改成功");
        } else {
            return Result.error("修改失败");
        }
    }

    /**
     * 删除员工
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        boolean success = employeeService.delete(id);
        if (success) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }

    /**
     * 批量删除员工
     */
    @DeleteMapping("/batch")
    public Result deleteBatch(@RequestBody List<Long> ids) {
        boolean success = employeeService.deleteBatch(ids);
        if (success) {
            return Result.success("批量删除成功");
        } else {
            return Result.error("批量删除失败");
        }
    }

    /**
     * 批量启用员工
     */
    @PutMapping("/enableBatch")
    public Result enableBatch(@RequestBody List<Long> ids) {
        boolean success = employeeService.enableBatch(ids);
        if (success) {
            return Result.success("批量启用成功");
        } else {
            return Result.error("批量启用失败");
        }
    }

    /**
     * 批量禁用员工
     */
    @PutMapping("/disableBatch")
    public Result disableBatch(@RequestBody List<Long> ids) {
        boolean success = employeeService.disableBatch(ids);
        if (success) {
            return Result.success("批量禁用成功");
        } else {
            return Result.error("批量禁用失败");
        }
    }

    /**
     * 生成员工编码
     */
    @GetMapping("/generateCode")
    public Result generateEmployeeCode(Long companyId) {
        String code = employeeService.generateEmployeeCode(companyId);
        return Result.success(code);
    }

    /**
     * 根据部门查询员工
     */
    @GetMapping("/byDepartment")
    public Result getByDepartment(String department, Long companyId) {
        List<Employee> list = employeeService.getByDepartment(department, companyId);
        return Result.success(list);
    }

    /**
     * 检查员工名称是否存在
     */
    @GetMapping("/checkName")
    public Result checkName(String employeeName, Long companyId, Long id) {
        boolean exists = employeeService.checkNameExists(employeeName, companyId, id);
        return Result.success(!exists);
    }

    /**
     * 检查身份证号是否存在
     */
    @GetMapping("/checkIdCard")
    public Result checkIdCard(String idCard, Long companyId, Long id) {
        boolean exists = employeeService.checkIdCardExists(idCard, companyId, id);
        return Result.success(!exists);
    }

    /**
     * 检查手机号是否存在
     */
    @GetMapping("/checkPhone")
    public Result checkPhone(String phone, Long companyId, Long id) {
        boolean exists = employeeService.checkPhoneExists(phone, companyId, id);
        return Result.success(!exists);
    }
}
