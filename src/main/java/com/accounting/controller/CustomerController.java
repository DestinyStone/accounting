package com.accounting.controller;

import com.accounting.common.Api;
import com.accounting.common.Result;
import com.accounting.entity.Customer;
import com.accounting.service.CustomerService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 客户控制器
 * 处理客户相关的CRUD操作
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
@RestController
@RequestMapping(Api.path + "/customer")
public class CustomerController {

    /**
     * 客户服务
     */
    @Autowired
    private CustomerService customerService;

    /**
     * JSON对象映射器
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 分页查询客户列表
     * 
     * @param pageNum 页码，默认为1
     * @param pageSize 每页大小，默认为10
     * @param companyId 公司ID（必填）
     * @param customerCode 客户编码（可选）
     * @param customerName 客户名称（可选）
     * @param status 状态（可选）
     * @return 分页结果
     */
    @GetMapping("/page")
    public Result<IPage<Customer>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = true) Long companyId,
            String customerCode,
            String customerName,
            Integer status) {
        try {
            IPage<Customer> page = customerService.page(pageNum, pageSize, companyId, customerCode, customerName, status);
            return Result.success(page);
        } catch (Exception e) {
            return Result.error("查询客户列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询客户
     * 
     * @param id 客户ID
     * @return 客户对象
     */
    @GetMapping("/getById/{id}")
    public Result<Customer> getById(@PathVariable Long id) {
        try {
            Customer customer = customerService.getById(id);
            if (customer != null) {
                return Result.success(customer);
            } else {
                return Result.error("客户不存在");
            }
        } catch (Exception e) {
            return Result.error("查询客户失败：" + e.getMessage());
        }
    }

    /**
     * 新增客户
     * 
     * @param customer 客户对象
     * @return 操作结果
     */
    @PostMapping("/insert")
    public Result insert(@RequestBody Customer customer) {
        try {
            boolean result = customerService.insert(customer);
            if (result) {
                return Result.success("新增客户成功");
            } else {
                return Result.error("新增客户失败");
            }
        } catch (Exception e) {
            return Result.error("新增客户失败：" + e.getMessage());
        }
    }

    /**
     * 更新客户
     * 
     * @param customer 客户对象
     * @return 操作结果
     */
    @PostMapping("/update")
    public Result update(@RequestBody Customer customer) {
        try {
            boolean result = customerService.update(customer);
            if (result) {
                return Result.success("更新客户成功");
            } else {
                return Result.error("更新客户失败");
            }
        } catch (Exception e) {
            return Result.error("更新客户失败：" + e.getMessage());
        }
    }

    /**
     * 删除客户
     * 
     * @param id 客户ID
     * @return 操作结果
     */
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        try {
            boolean result = customerService.delete(id);
            if (result) {
                return Result.success("删除客户成功");
            } else {
                return Result.error("删除客户失败");
            }
        } catch (Exception e) {
            return Result.error("删除客户失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除客户
     * 
     * @param ids 客户ID列表
     * @return 操作结果
     */
    @PostMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Long> ids) {
        try {
            boolean result = customerService.deleteBatch(ids);
            if (result) {
                return Result.success("批量删除客户成功");
            } else {
                return Result.error("批量删除客户失败");
            }
        } catch (Exception e) {
            return Result.error("批量删除客户失败：" + e.getMessage());
        }
    }

    /**
     * 根据客户编码查询
     * 
     * @param customerCode 客户编码
     * @param companyId 公司ID
     * @return 客户对象
     */
    @GetMapping("/getByCode")
    public Result<Customer> getByCode(String customerCode, Long companyId) {
        try {
            Customer customer = customerService.getByCode(customerCode, companyId);
            return Result.success(customer);
        } catch (Exception e) {
            return Result.error("查询客户失败：" + e.getMessage());
        }
    }

    /**
     * 根据公司ID查询启用的客户列表
     * 
     * @param companyId 公司ID
     * @return 客户列表
     */
    @GetMapping("/list")
    public Result<List<Customer>> list(Long companyId) {
        try {
            List<Customer> list = customerService.listActiveByCompanyId(companyId);
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("查询客户列表失败：" + e.getMessage());
        }
    }

    /**
     * 检查客户名称是否重复
     * 
     * @param customerName 客户名称
     * @param companyId 公司ID
     * @param id 客户ID（更新时传入，新增时可为null）
     * @return 是否重复（true-重复，false-不重复）
     */
    @GetMapping("/checkNameDuplicate")
    public Result<Boolean> checkNameDuplicate(String customerName, Long companyId, Long id) {
        try {
            boolean isDuplicate = customerService.checkNameDuplicate(customerName, companyId, id);
            return Result.success(isDuplicate);
        } catch (Exception e) {
            return Result.error("检查客户名称失败：" + e.getMessage());
        }
    }

    /**
     * 生成客户编码
     * 
     * @param companyId 公司ID
     * @return 生成的客户编码
     */
    @GetMapping("/generateCode")
    public Result<String> generateCode(Long companyId) {
        try {
            String code = customerService.generateCode(companyId);
            return Result.success(code);
        } catch (Exception e) {
            return Result.error("生成客户编码失败：" + e.getMessage());
        }
    }
}
