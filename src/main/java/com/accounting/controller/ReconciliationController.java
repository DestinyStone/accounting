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

import java.util.Date;

@RestController
@RequestMapping(Api.path + "/reconciliation")
public class ReconciliationController {

    @Autowired
    private ReconciliationService reconciliationService;

    @GetMapping("/page")
    public Result<IPage<Reconciliation>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false)Long accountId
    ) {
        try {
            IPage<Reconciliation> page = new Page<>(pageNum, pageSize);
            LambdaQueryWrapper<Reconciliation> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(accountId != null, Reconciliation::getAccountId, accountId);
            page = reconciliationService.page(page, wrapper);
            return Result.success(page);
        } catch (Exception e) {
            return Result.error("查询列表失败：" + e.getMessage());
        }
    }

    /**
     * 自动对账
     *
     * @param subjectId 科目ID
     * @param reconciliationDate 对账日期
     * @param statementBalance 对账单余额
     * @param remark 对账说明
     * @param userId 对账人
     * @return 对账记录
     */
    @PostMapping("/auto")
    public Response autoReconcile(
            @RequestParam Long subjectId,
            @RequestParam String reconciliationDate,
            @RequestParam String statementBalance,
            @RequestParam(required = false) String remark,
            @RequestParam(required = false) Long userId) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            Date recDate = sdf.parse(reconciliationDate);

            // 安全解析对账单余额
            java.math.BigDecimal stmtBalance;
            try {
                stmtBalance = new java.math.BigDecimal(statementBalance.trim());
            } catch (Exception ex) {
                return Response.error("对账单余额格式不正确，请输入数字");
            }

            Reconciliation reconciliation = reconciliationService.autoReconcile(subjectId, recDate, stmtBalance, remark, userId);
            return Response.success(reconciliation);
        } catch (Exception e) {
            return Response.error("自动对账失败：" + e.getMessage());
        }
    }

    /**
     * 计算账户余额
     *
     * @param subjectId 科目ID
     * @param endDate 截止日期（可选）
     * @return 账户余额
     */
    @GetMapping("/balance")
    public Response calculateBalance(
            @RequestParam Long subjectId,
            @RequestParam(required = false) String endDate) {
        try {
            Date date = null;
            if (endDate != null && !endDate.isEmpty()) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                date = sdf.parse(endDate);
            }
            java.math.BigDecimal balance = reconciliationService.calculateAccountBalance(subjectId, date);
            return Response.success(balance);
        } catch (Exception e) {
            return Response.error("计算余额失败：" + e.getMessage());
        }
    }

    /**
     * 获取对账详情
     *
     * @param id 对账记录ID
     * @return 对账详情
     */
    @GetMapping("/detail/{id}")
    public Response getDetail(@PathVariable Long id) {
        try {
            return Response.success(reconciliationService.getReconciliationDetail(id));
        } catch (Exception e) {
            return Response.error("获取对账详情失败：" + e.getMessage());
        }
    }

    // 已移除：手动新增对账记录功能（对账记录只能通过自动对账生成）
    // 已移除：手动更新对账记录功能（对账记录不允许修改）

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

    /**
     * 删除对账记录
     *
     * @param id 对账记录ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Response delete(@PathVariable Long id) {
        try {
            boolean success = reconciliationService.removeById(id);
            return success ? Response.success("删除成功") : Response.error("删除失败");
        } catch (Exception e) {
            return Response.error("删除失败：" + e.getMessage());
        }
    }
}
