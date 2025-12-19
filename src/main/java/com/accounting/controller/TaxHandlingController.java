package com.accounting.controller;

import cn.hutool.core.util.StrUtil;
import com.accounting.common.Api;
import com.accounting.common.Response;
import com.accounting.common.Result;
import com.accounting.entity.RegularBusiness;
import com.accounting.entity.TaxHandling;
import com.accounting.service.TaxHandlingService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Api.path + "/tax-handling")
public class TaxHandlingController {

    @Autowired
    private TaxHandlingService taxHandlingService;

    @GetMapping("/page")
    public Result<IPage<TaxHandling>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false)String taxType
    ) {
        try {
            IPage<TaxHandling> page = new Page<>(pageNum, pageSize);
            LambdaQueryWrapper<TaxHandling> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(StrUtil.isNotBlank(taxType), TaxHandling::getTaxType, taxType);
            page = taxHandlingService.page(page, wrapper);
            return Result.success(page);
        } catch (Exception e) {
            return Result.error("查询列表失败：" + e.getMessage());
        }
    }

    @PostMapping
    public Response save(@RequestBody TaxHandling taxHandling) {
        try {
            TaxHandling saved = taxHandlingService.saveTaxHandling(taxHandling);
            return Response.success(saved);
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    @PutMapping
    public Response update(@RequestBody TaxHandling taxHandling) {
        try {
            TaxHandling updated = taxHandlingService.updateTaxHandling(taxHandling);
            return Response.success(updated);
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Response getById(@PathVariable Long id) {
        return Response.success(taxHandlingService.getById(id));
    }

    @GetMapping
    public Response list() {
        return Response.success(taxHandlingService.list());
    }

    @PostMapping("/declare/{id}")
    public Response declare(@PathVariable Long id) {
        try {
            return Response.success(taxHandlingService.declareTax(id));
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    @PostMapping("/pay/{id}")
    public Response pay(@PathVariable Long id) {
        try {
            return Response.success(taxHandlingService.payTax(id));
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Response delete(@PathVariable Long id) {
        return Response.success(taxHandlingService.removeById(id));
    }
}
