package com.accounting.controller;

import com.accounting.common.Api;
import com.accounting.common.Result;
import com.accounting.entity.Supplier;
import com.accounting.service.SupplierService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * 供应商控制器
 */
@RestController
@RequestMapping(Api.path + "/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    /**
     * 分页查询供应商列表
     */
    @GetMapping("/page")
    public Result page(@RequestParam Map<String, Object> params) {
        try {
            // 获取分页参数
            int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;

            // 分页查询
            IPage<Supplier> page = supplierService.selectPage(new Page<>(pageNum, pageSize), params);

            return Result.success(page);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据ID查询供应商信息
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        try {
            Supplier supplier = supplierService.selectById(id);
            if (supplier == null) {
                return Result.error("供应商不存在");
            }
            return Result.success(supplier);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 新增供应商
     */
    @PostMapping
    public Result insert(@RequestBody Supplier supplier) {
        try {
            // 检查供应商名称是否已存在
            if (supplierService.checkNameExists(supplier.getSupplierName(), null)) {
                return Result.error("供应商名称已存在");
            }

            boolean result = supplierService.insert(supplier);
            if (result) {
                return Result.success("新增成功");
            } else {
                return Result.error("新增失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新供应商信息
     */
    @PutMapping
    public Result update(@RequestBody Supplier supplier) {
        try {
            // 检查供应商名称是否已存在
            if (supplierService.checkNameExists(supplier.getSupplierName(), supplier.getId())) {
                return Result.error("供应商名称已存在");
            }

            boolean result = supplierService.update(supplier);
            if (result) {
                return Result.success("更新成功");
            } else {
                return Result.error("更新失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除供应商
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        try {
            boolean result = supplierService.delete(id);
            if (result) {
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 批量删除供应商
     */
    @DeleteMapping("/batch")
    public Result deleteBatch(@RequestBody List<Long> ids) {
        try {
            boolean result = supplierService.deleteBatch(ids);
            if (result) {
                return Result.success("批量删除成功");
            } else {
                return Result.error("批量删除失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据公司ID查询启用的供应商列表
     */
    @GetMapping("/list")
    public Result list() {
        try {
            LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Supplier::getStatus, 1);
            List<Supplier> list = supplierService.list(wrapper);
            return Result.success(list);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 检查供应商名称是否已存在
     */
    @GetMapping("/checkName")
    public Result checkName(@RequestParam String supplierName, @RequestParam(required = false) Long excludeId) {
        try {
            boolean exists = supplierService.checkNameExists(supplierName, excludeId);
            return Result.success(!exists);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
