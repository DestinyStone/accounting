package com.accounting.controller;

import cn.hutool.core.util.StrUtil;
import com.accounting.common.Api;
import com.accounting.common.Response;
import com.accounting.common.Result;
import com.accounting.entity.JournalEntry;
import com.accounting.entity.Posting;
import com.accounting.entity.RegularBusiness;
import com.accounting.entity.SalesOrder;
import com.accounting.mapper.JournalEntryMapper;
import com.accounting.service.JournalEntryService;
import com.accounting.service.PostingService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * 过账控制器
 * 处理凭证过账相关的操作
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
@RestController
@RequestMapping(Api.path + "/posting")
public class PostingController {

    /**
     * 过账服务
     */
    @Autowired
    private PostingService postingService;

    /**
     * 分页查询过账列表
     * 
     * @param pageNum 页码，默认为1
     * @param pageSize 每页大小，默认为10
     * @param entryId 凭证分录ID（可选）
     * @return 分页结果
     */
    @GetMapping("/page")
    public Result<IPage<Posting>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false)String entryId
    ) {
        try {
            IPage<Posting> page = new Page<>(pageNum, pageSize);
            LambdaQueryWrapper<Posting> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(StrUtil.isNotBlank(entryId), Posting::getEntryId, entryId);
            page = postingService.page(page, wrapper);
            return Result.success(page);
        } catch (Exception e) {
            return Result.error("查询列表失败：" + e.getMessage());
        }
    }

    /**
     * 凭证分录Mapper
     */
    @Autowired
    private JournalEntryMapper entryMapper;

    /**
     * 新增过账记录
     * 
     * @param posting 过账对象
     * @return 操作结果
     */
    @PostMapping("/insert")
    @Transactional
    public Result insert(@RequestBody Posting posting) {

        JournalEntry journalEntry = entryMapper.selectById(posting.getEntryId());
        if (journalEntry.getStatus() == 1) {
            return Result.error("分录已过账");
        }

        entryMapper.updateStatus(posting.getEntryId(), 1);
        postingService.save(posting);
        return Result.success("新增成功");
    }

    /**
     * 更新过账记录
     * 
     * @param posting 过账对象
     * @return 操作结果
     */
    @PostMapping("/update")
    public Result update(@RequestBody Posting posting) {
        postingService.updateById(posting);
        return Result.success("新增成功");
    }

    /**
     * 执行过账操作
     * 
     * @param entryId 凭证分录ID
     * @param userId 用户ID
     * @return 操作结果
     */
    @PostMapping("/{entryId}")
    public Response post(@PathVariable Long entryId, @RequestParam String userId) {
        try {
            boolean success = postingService.postJournalEntry(entryId, userId);
            return success ? Response.success() : Response.error("该凭证已过账或不存在");
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    /**
     * 取消过账
     * 
     * @param entryId 凭证分录ID
     * @return 操作结果
     */
    @DeleteMapping("/{entryId}")
    public Response cancel(@PathVariable Long entryId) {
        try {
            boolean success = postingService.cancelPosting(entryId);
            return success ? Response.success() : Response.error("取消过账失败");
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    /**
     * 根据凭证分录ID查询过账记录
     * 
     * @param entryId 凭证分录ID
     * @return 过账记录
     */
    @GetMapping("/entry/{entryId}")
    public Response getByEntryId(@PathVariable Long entryId) {
        return Response.success(postingService.getByEntryId(entryId));
    }
}
