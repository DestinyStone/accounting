package com.accounting.controller;

import cn.hutool.core.util.StrUtil;
import com.accounting.common.Api;
import com.accounting.common.Response;
import com.accounting.common.Result;
import com.accounting.entity.RegularBusiness;
import com.accounting.entity.SalesOrder;
import com.accounting.service.RegularBusinessService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Api.path + "/regular-business")
public class RegularBusinessController {

    @Autowired
    private RegularBusinessService regularBusinessService;

    @GetMapping("/page")
    public Result<IPage<RegularBusiness>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false)String businessType
           ) {
        try {
            IPage<RegularBusiness> page = new Page<>(pageNum, pageSize);
            LambdaQueryWrapper<RegularBusiness> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(StrUtil.isNotBlank(businessType), RegularBusiness::getBusinessType, businessType);
            page = regularBusinessService.page(page, wrapper);
            return Result.success(page);
        } catch (Exception e) {
            return Result.error("查询列表失败：" + e.getMessage());
        }
    }

    @PostMapping
    public Response save(@RequestBody RegularBusiness regularBusiness) {
        try {
            RegularBusiness saved = regularBusinessService.saveRegularBusiness(regularBusiness);
            return Response.success(saved);
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    @PutMapping
    public Response update(@RequestBody RegularBusiness regularBusiness) {
        try {
            RegularBusiness updated = regularBusinessService.updateRegularBusiness(regularBusiness);
            return Response.success(updated);
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Response getById(@PathVariable Long id) {
        return Response.success(regularBusinessService.getById(id));
    }

    @GetMapping
    public Response list() {
        return Response.success(regularBusinessService.list());
    }

    @PostMapping("/process/{id}")
    public Response process(@PathVariable Long id) {
        try {
            return Response.success(regularBusinessService.processBusiness(id));
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Response delete(@PathVariable Long id) {
        return Response.success(regularBusinessService.removeById(id));
    }
}
