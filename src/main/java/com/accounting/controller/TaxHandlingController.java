package com.accounting.controller;

import cn.hutool.core.util.StrUtil;
import com.accounting.common.Api;
import com.accounting.common.Response;
import com.accounting.common.Result;
import com.accounting.entity.RegularBusiness;
import com.accounting.entity.TaxHandling;
import com.accounting.entity.TaxHandlingVO;
import com.accounting.service.TaxHandlingService;
import com.accounting.service.impl.TaxHandlingServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(Api.path + "/tax-handling")
public class TaxHandlingController {

    @Autowired
    private TaxHandlingService taxHandlingService;
    
    @Autowired
    private TaxHandlingServiceImpl taxHandlingServiceImpl;

    @GetMapping("/page")
    public Result<IPage<TaxHandlingVO>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false)String taxType
    ) {
        try {
            IPage<TaxHandling> page = new Page<>(pageNum, pageSize);
            LambdaQueryWrapper<TaxHandling> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(StrUtil.isNotBlank(taxType), TaxHandling::getTaxType, taxType);
            page = taxHandlingService.page(page, wrapper);
            
            // 转换为VO，添加业务来源信息
            IPage<TaxHandlingVO> voPage = new Page<>(pageNum, pageSize, page.getTotal());
            List<TaxHandlingVO> voList = new ArrayList<>();
            for (TaxHandling tax : page.getRecords()) {
                TaxHandlingVO vo = new TaxHandlingVO();
                BeanUtils.copyProperties(tax, vo);
                vo.setBusinessSourceInfo(taxHandlingServiceImpl.getBusinessSourceInfo(tax));
                voList.add(vo);
            }
            voPage.setRecords(voList);
            
            return Result.success(voPage);
        } catch (Exception e) {
            return Result.error("查询列表失败：" + e.getMessage());
        }
    }

    // 已移除：手动新增税务记录功能（只能通过业务自动生成）
    // 已移除：手动更新税务记录功能（系统生成的记录不允许修改）

    @GetMapping("/{id}")
    public Response getById(@PathVariable Long id) {
        TaxHandling tax = taxHandlingService.getById(id);
        if (tax != null) {
            TaxHandlingVO vo = new TaxHandlingVO();
            BeanUtils.copyProperties(tax, vo);
            vo.setBusinessSourceInfo(taxHandlingServiceImpl.getBusinessSourceInfo(tax));
            return Response.success(vo);
        }
        return Response.error("税务记录不存在");
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

    // 已移除：删除税务记录功能（系统生成的记录不允许删除）
}
