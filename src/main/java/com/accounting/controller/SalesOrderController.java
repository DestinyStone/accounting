package com.accounting.controller;

import com.accounting.common.Api;
import com.accounting.common.Result;
import com.accounting.entity.SalesOrder;
import com.accounting.service.SalesOrderService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 销售订单控制器
 */
@RestController
@RequestMapping(Api.path + "/salesOrder")
public class SalesOrderController {

    @Autowired
    private SalesOrderService salesOrderService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 分页查询销售订单列表
     */
    @GetMapping("/page")
    public Result<IPage<SalesOrder>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            String orderNumber,
            Long customerId,
            Integer status,
            String startDate,
            String endDate) {
        try {
            IPage<SalesOrder> page = salesOrderService.page(pageNum, pageSize, 1L, orderNumber, customerId, status, startDate, endDate);
            return Result.success(page);
        } catch (Exception e) {
            return Result.error("查询销售订单列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询销售订单及明细
     */
    @GetMapping("/getById/{id}")
    public Result<Map<String, Object>> getById(@PathVariable Long id) {
        try {
            Map<String, Object> result = salesOrderService.getById(id);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("查询销售订单失败：" + e.getMessage());
        }
    }

    /**
     * 新增销售订单
     */
    @PostMapping("/insert")
    public Result insert(@RequestBody SalesOrder order) {
        try {
            // 解析请求参数
            boolean result = salesOrderService.insert(order);
            if (result) {
                return Result.success("新增销售订单成功");
            } else {
                return Result.error("新增销售订单失败");
            }
        } catch (Exception e) {
            return Result.error("新增销售订单失败：" + e.getMessage());
        }
    }

    /**
     * 更新销售订单
     */
    @PostMapping("/update")
    public Result update(@RequestBody SalesOrder order) {
        try {
            // 解析请求参数
            boolean result = salesOrderService.update(order);
            if (result) {
                return Result.success("更新销售订单成功");
            } else {
                return Result.error("更新销售订单失败");
            }
        } catch (Exception e) {
            return Result.error("更新销售订单失败：" + e.getMessage());
        }
    }

    /**
     * 删除销售订单
     */
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        try {
            boolean result = salesOrderService.delete(id);
            if (result) {
                return Result.success("删除销售订单成功");
            } else {
                return Result.error("删除销售订单失败");
            }
        } catch (Exception e) {
            return Result.error("删除销售订单失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除销售订单
     */
    @PostMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Long> ids) {
        try {
            boolean result = salesOrderService.deleteBatch(ids);
            if (result) {
                return Result.success("批量删除销售订单成功");
            } else {
                return Result.error("批量删除销售订单失败");
            }
        } catch (Exception e) {
            return Result.error("批量删除销售订单失败：" + e.getMessage());
        }
    }

    /**
     * 提交销售订单
     */
    @PostMapping("/submit/{id}")
    public Result submit(@PathVariable Long id, @RequestBody Map<String, Long> params) {
        try {
            Long userId = params.get("userId");
            boolean result = salesOrderService.submit(id, userId);
            if (result) {
                return Result.success("提交销售订单成功");
            } else {
                return Result.error("提交销售订单失败");
            }
        } catch (Exception e) {
            return Result.error("提交销售订单失败：" + e.getMessage());
        }
    }

    /**
     * 审核销售订单
     */
    @PostMapping("/approve/{id}")
    public Result approve(@PathVariable Long id, @RequestBody Map<String, Long> params) {
        try {
            Long userId = params.get("userId");
            boolean result = salesOrderService.approve(id, userId);
            if (result) {
                return Result.success("审核销售订单成功");
            } else {
                return Result.error("审核销售订单失败");
            }
        } catch (Exception e) {
            return Result.error("审核销售订单失败：" + e.getMessage());
        }
    }

    /**
     * 取消销售订单
     */
    @PostMapping("/cancel/{id}")
    public Result cancel(@PathVariable Long id, @RequestBody Map<String, Long> params) {
        try {
            Long userId = params.get("userId");
            boolean result = salesOrderService.cancel(id, userId);
            if (result) {
                return Result.success("取消销售订单成功");
            } else {
                return Result.error("取消销售订单失败");
            }
        } catch (Exception e) {
            return Result.error("取消销售订单失败：" + e.getMessage());
        }
    }
}
