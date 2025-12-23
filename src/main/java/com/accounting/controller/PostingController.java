package com.accounting.controller;

import cn.hutool.core.util.StrUtil;
import com.accounting.common.Api;
import com.accounting.common.Response;
import com.accounting.common.Result;
import com.accounting.entity.JournalEntry;
import com.accounting.entity.Posting;
import com.accounting.entity.PostingVO;
import com.accounting.entity.RegularBusiness;
import com.accounting.entity.SalesOrder;
import com.accounting.mapper.JournalEntryMapper;
import com.accounting.service.JournalEntryService;
import com.accounting.service.PostingService;
import com.accounting.service.impl.PostingServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    
    @Autowired
    private PostingServiceImpl postingServiceImpl;

    /**
     * 分页查询过账列表
     * 
     * @param pageNum 页码，默认为1
     * @param pageSize 每页大小，默认为10
     * @param entryId 凭证分录ID（可选）
     * @return 分页结果
     */
    @GetMapping("/page")
    public Result<IPage<PostingVO>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false)String entryId
    ) {
        try {
            IPage<Posting> page = new Page<>(pageNum, pageSize);
            LambdaQueryWrapper<Posting> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(StrUtil.isNotBlank(entryId), Posting::getEntryId, entryId);
            wrapper.orderByDesc(Posting::getPostingDate);
            page = postingService.page(page, wrapper);
            
            // 转换为VO，添加业务来源信息
            IPage<PostingVO> voPage = new Page<>(pageNum, pageSize, page.getTotal());
            List<PostingVO> voList = new ArrayList<>();
            for (Posting posting : page.getRecords()) {
                PostingVO vo = new PostingVO();
                BeanUtils.copyProperties(posting, vo);
                vo.setBusinessSourceInfo(postingServiceImpl.getBusinessSourceInfo(posting));
                voList.add(vo);
            }
            voPage.setRecords(voList);
            
            return Result.success(voPage);
        } catch (Exception e) {
            return Result.error("查询列表失败：" + e.getMessage());
        }
    }

    /**
     * 凭证分录Mapper
     */
    @Autowired
    private JournalEntryMapper entryMapper;
    
    @Autowired
    private JournalEntryService journalEntryService;

    // 已移除：手动新增过账记录功能（请使用post方法进行过账操作）
    // 已移除：手动更新过账记录功能（过账记录不允许修改）

    /**
     * 执行过账操作
     * 
     * @param entryId 凭证分录ID
     * @param userId 用户ID
     * @return 操作结果
     */
    @PostMapping("/{entryId}")
    public Response post(@PathVariable Long entryId, @RequestParam Long userId) {
        try {
            boolean success = postingService.postJournalEntry(entryId, userId);
            return success ? Response.success("过账成功") : Response.error("过账失败");
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

    /**
     * 获取过账详情（包含凭证信息、凭证明细、业务来源等）
     * 
     * @param id 过账记录ID
     * @return 过账详情
     */
    @GetMapping("/detail/{id}")
    public Response getDetail(@PathVariable Long id) {
        try {
            Posting posting = postingService.getById(id);
            if (posting == null) {
                return Response.error("过账记录不存在");
            }
            
            com.accounting.entity.PostingDetailVO detailVO = new com.accounting.entity.PostingDetailVO();
            detailVO.setPosting(posting);
            
            // 查询凭证信息
            JournalEntry journalEntry = entryMapper.selectByIdWithDetails(posting.getEntryId());
            if (journalEntry == null) {
                return Response.error("凭证不存在");
            }
            detailVO.setJournalEntry(journalEntry);
            
            // 查询凭证明细（包含科目信息）
            java.util.List<java.util.Map<String, Object>> details = journalEntryService.getDetailsWithSubject(posting.getEntryId());
            detailVO.setEntryDetails(details);
            
            // 获取业务来源信息
            String businessSourceInfo = postingServiceImpl.getBusinessSourceInfo(posting);
            detailVO.setBusinessSourceInfo(businessSourceInfo);
            
            return Response.success(detailVO);
        } catch (Exception e) {
            return Response.error("获取过账详情失败：" + e.getMessage());
        }
    }
}
