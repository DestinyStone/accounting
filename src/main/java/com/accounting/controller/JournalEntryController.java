package com.accounting.controller;

import cn.hutool.core.util.StrUtil;
import com.accounting.common.Api;
import com.accounting.common.Response;
import com.accounting.common.Result;
import com.accounting.entity.JournalEntry;
import com.accounting.entity.JournalEntryDetail;
import com.accounting.entity.TaxHandling;
import com.accounting.mapper.JournalEntryDetailMapper;
import com.accounting.service.JournalEntryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 凭证分录控制器
 * 处理凭证分录相关的CRUD操作
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
@RestController
@RequestMapping(Api.path + "/journal-entry")
public class JournalEntryController {

    /**
     * 凭证分录服务
     */
    @Autowired
    private JournalEntryService journalEntryService;

    /**
     * 凭证分录明细Mapper
     */
    @Autowired
    private JournalEntryDetailMapper detailMapper;

    /**
     * 分页查询凭证分录列表
     * 
     * @param pageNum 页码，默认为1
     * @param pageSize 每页大小，默认为10
     * @param voucherNo 凭证号（可选）
     * @return 分页结果
     */
    @GetMapping("/page")
    public Result<IPage<JournalEntry>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false)String voucherNo
    ) {
        try {
            IPage<JournalEntry> page = new Page<>(pageNum, pageSize);
            LambdaQueryWrapper<JournalEntry> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(StrUtil.isNotBlank(voucherNo), JournalEntry::getVoucherNo, voucherNo);
            page = journalEntryService.page(page, wrapper);
            return Result.success(page);
        } catch (Exception e) {
            return Result.error("查询列表失败：" + e.getMessage());
        }
    }

    /**
     * 保存凭证分录
     * 
     * @param journalEntry 凭证分录对象
     * @return 保存结果
     */
    @PostMapping
    public Response save(@RequestBody JournalEntry journalEntry) {
        try {
            JournalEntry saved = journalEntryService.saveEntry(journalEntry);
            return Response.success(saved);
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    /**
     * 更新凭证分录
     * 
     * @param journalEntry 凭证分录对象
     * @return 更新结果
     */
    @PutMapping
    public Response update(@RequestBody JournalEntry journalEntry) {
        try {
            JournalEntry updated = journalEntryService.updateEntry(journalEntry);
            return Response.success(updated);
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    /**
     * 根据ID查询凭证分录（包含明细）
     * 
     * @param id 凭证分录ID
     * @return 凭证分录对象
     */
    @GetMapping("/{id}")
    public Response getById(@PathVariable Long id) {
        JournalEntry journalEntry = journalEntryService.getByIdWithDetails(id);
        return Response.success(journalEntry);
    }

    /**
     * 根据条件查询凭证分录（包含明细）
     * 
     * @param params 查询参数
     * @return 凭证分录列表
     */
    @GetMapping
    public Response query(@RequestParam Map<String, Object> params) {
        return Response.success(journalEntryService.queryWithDetails(params));
    }

    /**
     * 根据凭证分录ID查询明细列表
     * 
     * @param id 凭证分录ID
     * @return 明细列表
     */
    @GetMapping("/detail/{id}")
    public Response detail(@PathVariable Long id) {

        LambdaQueryWrapper<JournalEntryDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(JournalEntryDetail::getEntryId, id);
        return Response.success(detailMapper.selectList(wrapper));
    }

    /**
     * 删除凭证分录
     * 
     * @param id 凭证分录ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Response delete(@PathVariable Long id) {
        return Response.success(journalEntryService.deleteEntry(id));
    }

    /**
     * 生成凭证号
     * 
     * @param date 日期字符串
     * @return 生成的凭证号
     */
    @GetMapping("/generate-voucher-no/{date}")
    public Response generateVoucherNo(@PathVariable String date) {
        return Response.success(journalEntryService.generateVoucherNo(date));
    }
}
