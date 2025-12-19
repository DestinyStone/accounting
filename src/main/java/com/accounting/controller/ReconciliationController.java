package com.accounting.controller;

import cn.hutool.core.util.StrUtil;
import com.accounting.common.Api;
import com.accounting.common.Response;
import com.accounting.common.Result;
import com.accounting.entity.JournalEntry;
import com.accounting.entity.Reconciliation;
import com.accounting.service.ReconciliationService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Api.path + "/reconciliation")
public class ReconciliationController {

    @Autowired
    private ReconciliationService reconciliationService;

    @GetMapping("/page")
    public Result<IPage<Reconciliation>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false)String accountId
    ) {
        try {
            IPage<Reconciliation> page = new Page<>(pageNum, pageSize);
            LambdaQueryWrapper<Reconciliation> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(StrUtil.isNotBlank(accountId), Reconciliation::getAccountId, accountId);
            page = reconciliationService.page(page, wrapper);
            return Result.success(page);
        } catch (Exception e) {
            return Result.error("查询列表失败：" + e.getMessage());
        }
    }

    @PostMapping
    public Response save(@RequestBody Reconciliation reconciliation) {
        try {
            Reconciliation saved = reconciliationService.createReconciliation(reconciliation);
            return Response.success(saved);
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    @PutMapping
    public Response update(@RequestBody Reconciliation reconciliation) {
        try {
            Reconciliation updated = reconciliationService.updateReconciliation(reconciliation);
            return Response.success(updated);
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Response getById(@PathVariable Long id) {
        return Response.success(reconciliationService.getById(id));
    }

    @GetMapping
    public Response list() {
        return Response.success(reconciliationService.list());
    }

    @PostMapping("/complete/{id}")
    public Response complete(@PathVariable Long id) {
        try {
            return Response.success(reconciliationService.completeReconciliation(id));
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public Response delete(@PathVariable Long id) {
        return Response.success(reconciliationService.removeById(id));
    }
}
