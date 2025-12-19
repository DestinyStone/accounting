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

@RestController
@RequestMapping(Api.path + "/journal-entry")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private JournalEntryDetailMapper detailMapper;

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

    @PostMapping
    public Response save(@RequestBody JournalEntry journalEntry) {
        try {
            JournalEntry saved = journalEntryService.saveEntry(journalEntry);
            return Response.success(saved);
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    @PutMapping
    public Response update(@RequestBody JournalEntry journalEntry) {
        try {
            JournalEntry updated = journalEntryService.updateEntry(journalEntry);
            return Response.success(updated);
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Response getById(@PathVariable Long id) {
        JournalEntry journalEntry = journalEntryService.getByIdWithDetails(id);
        return Response.success(journalEntry);
    }

    @GetMapping
    public Response query(@RequestParam Map<String, Object> params) {
        return Response.success(journalEntryService.queryWithDetails(params));
    }

    @GetMapping("/detail/{id}")
    public Response detail(@PathVariable Long id) {

        LambdaQueryWrapper<JournalEntryDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(JournalEntryDetail::getEntryId, id);
        return Response.success(detailMapper.selectList(wrapper));
    }


    @DeleteMapping("/{id}")
    public Response delete(@PathVariable Long id) {
        return Response.success(journalEntryService.deleteEntry(id));
    }

    @GetMapping("/generate-voucher-no/{date}")
    public Response generateVoucherNo(@PathVariable String date) {
        return Response.success(journalEntryService.generateVoucherNo(date));
    }
}
