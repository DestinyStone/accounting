package com.accounting.controller;

import com.accounting.common.Api;
import com.accounting.common.Result;
import com.accounting.entity.PurchaseOrder;
import com.accounting.service.PurchaseOrderService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 采购订单控制器
 */
@RestController
@RequestMapping(Api.path + "/purchase-order")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    /**
     * 分页查询采购订单列表
     */
    @GetMapping("/page")
    public Result page(@RequestParam Map<String, Object> params) {
        try {
            // 获取分页参数
            int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;

            // 分页查询
            IPage<PurchaseOrder> page = purchaseOrderService.selectPage(new Page<>(pageNum, pageSize), params);

            return Result.success(page);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 新增采购订单
     */
    @PostMapping
    public Result insert(@RequestBody PurchaseOrder order) {
        order.setOrderDate(LocalDateTime.now());
        purchaseOrderService.save(order);
        return Result.success("新增成功");
    }

    /**
     * 更新采购订单
     */
    @PutMapping
    public Result update(@RequestBody PurchaseOrder order) {
        purchaseOrderService.updateById(order);
        return Result.success("新增成功");
    }

    /**
     * 删除采购订单
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        try {
            boolean result = purchaseOrderService.delete(id);
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
     * 批量删除采购订单
     */
    @DeleteMapping("/batch")
    public Result deleteBatch(@RequestBody List<Long> ids) {
        try {
            boolean result = purchaseOrderService.deleteBatch(ids);
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
     * 提交采购订单
     */
    @PostMapping("/submit/{id}")
    public Result submit(@PathVariable Long id) {
        try {
            boolean result = purchaseOrderService.submit(id);
            if (result) {
                return Result.success("提交成功");
            } else {
                return Result.error("提交失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 审核采购订单
     */
    @PostMapping("/approve/{id}")
    public Result approve(@PathVariable Long id) {
        try {
            boolean result = purchaseOrderService.approve(id);
            if (result) {
                return Result.success("审核成功");
            } else {
                return Result.error("审核失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 取消采购订单
     */
    @PostMapping("/cancel/{id}")
    public Result cancel(@PathVariable Long id) {
        try {
            boolean result = purchaseOrderService.cancel(id);
            if (result) {
                return Result.success("取消成功");
            } else {
                return Result.error("取消失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 生成采购订单编号
     */
    @GetMapping("/generateNumber")
    public Result generateNumber(@RequestParam Long companyId) {
        try {
            String orderNumber = purchaseOrderService.generateOrderNumber(companyId);
            return Result.success(orderNumber);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
