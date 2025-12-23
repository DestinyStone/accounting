package com.accounting.controller;

import com.accounting.common.Api;
import com.accounting.common.Result;
import com.accounting.entity.AccountingSubject;
import com.accounting.service.AccountingSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 会计科目控制器
 * 处理会计科目相关的CRUD操作和树形结构查询
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
@RestController
@RequestMapping(Api.path + "/accounting-subject")
public class AccountingSubjectController {

    /**
     * 会计科目服务
     */
    @Autowired
    private AccountingSubjectService accountingSubjectService;

    /**
     * 获取科目树形结构
     */
    @GetMapping("/tree")
    public Result<List<AccountingSubject>> getSubjectTree() {
        try {
            List<AccountingSubject> subjectTree = accountingSubjectService.getSubjectTree();
            return Result.success(subjectTree);
        } catch (Exception e) {
            return Result.error("获取科目树失败：" + e.getMessage());
        }
    }

    /**
     * 根据父级ID获取子科目
     * 
     * @param parentId 父级科目ID
     * @return 子科目列表
     */
    @GetMapping("/children/{parentId}")
    public Result<List<AccountingSubject>> getChildrenByParentId(@PathVariable Long parentId) {
        try {
            List<AccountingSubject> children = accountingSubjectService.getChildrenByParentId(parentId);
            return Result.success(children);
        } catch (Exception e) {
            return Result.error("获取子科目失败：" + e.getMessage());
        }
    }

    /**
     * 新增科目
     * 
     * @param subject 会计科目对象
     * @return 操作结果
     */
    @PostMapping("/add")
    public Result<Boolean> addSubject(@RequestBody AccountingSubject subject) {
        try {
            boolean success = accountingSubjectService.addSubject(subject);
            if (success) {
                return Result.success(true);
            } else {
                return Result.error("添加科目失败");
            }
        } catch (Exception e) {
            return Result.error("添加科目失败：" + e.getMessage());
        }
    }

    /**
     * 更新科目
     * 
     * @param subject 会计科目对象
     * @return 操作结果
     */
    @PutMapping("/update")
    public Result<Boolean> updateSubject(@RequestBody AccountingSubject subject) {
        try {
            boolean success = accountingSubjectService.updateSubject(subject);
            if (success) {
                return Result.success(true);
            } else {
                return Result.error("更新科目失败");
            }
        } catch (Exception e) {
            return Result.error("更新科目失败：" + e.getMessage());
        }
    }

    /**
     * 删除科目
     * 
     * @param id 科目ID
     * @return 操作结果
     */
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> deleteSubject(@PathVariable Long id) {
        try {
            boolean success = accountingSubjectService.deleteSubject(id);
            if (success) {
                return Result.success(true);
            } else {
                return Result.error("该科目有子科目，无法删除");
            }
        } catch (Exception e) {
            return Result.error("删除科目失败：" + e.getMessage());
        }
    }

    /**
     * 启用/禁用科目
     * 
     * @param id 科目ID
     * @param enabled 是否启用（true-启用，false-禁用）
     * @return 操作结果
     */
    @PutMapping("/enable/{id}")
    public Result<Boolean> enableSubject(@PathVariable Long id, @RequestParam Boolean enabled) {
        try {
            boolean success = accountingSubjectService.enableSubject(id, enabled);
            if (success) {
                return Result.success(true);
            } else {
                return Result.error("操作失败");
            }
        } catch (Exception e) {
            return Result.error("操作失败：" + e.getMessage());
        }
    }

    /**
     * 获取所有启用的末级科目
     */
    @GetMapping("/leaf-subjects")
    public Result<List<AccountingSubject>> getEnabledLeafSubjects() {
        try {
            List<AccountingSubject> leafSubjects = accountingSubjectService.getEnabledLeafSubjects();
            return Result.success(leafSubjects);
        } catch (Exception e) {
            return Result.error("获取末级科目失败：" + e.getMessage());
        }
    }

    /**
     * 绑定科目到业务类型
     * 
     * @param id 科目ID
     * @param businessType 业务类型（PURCHASE-采购单，SALES-销售单，EXPENSE-员工费用，TAX-税务处理）
     * @return 操作结果
     */
    @PostMapping("/bind/{id}")
    public Result<Boolean> bindBusinessType(@PathVariable Long id, @RequestParam String businessType) {
        try {
            boolean success = accountingSubjectService.bindBusinessType(id, businessType);
            if (success) {
                return Result.success(true);
            } else {
                return Result.error("绑定失败");
            }
        } catch (Exception e) {
            return Result.error("绑定失败：" + e.getMessage());
        }
    }

    /**
     * 取消科目绑定
     * 
     * @param id 科目ID
     * @return 操作结果
     */
    @PostMapping("/unbind/{id}")
    public Result<Boolean> unbindBusinessType(@PathVariable Long id) {
        try {
            boolean success = accountingSubjectService.unbindBusinessType(id);
            if (success) {
                return Result.success(true);
            } else {
                return Result.error("取消绑定失败");
            }
        } catch (Exception e) {
            return Result.error("取消绑定失败：" + e.getMessage());
        }
    }

    /**
     * 根据业务类型获取绑定的科目
     * 
     * @param businessType 业务类型
     * @return 绑定的科目
     */
    @GetMapping("/getByBusinessType/{businessType}")
    public Result<AccountingSubject> getByBusinessType(@PathVariable String businessType) {
        try {
            AccountingSubject subject = accountingSubjectService.getByBusinessType(businessType);
            return Result.success(subject);
        } catch (Exception e) {
            return Result.error("获取绑定科目失败：" + e.getMessage());
        }
    }
}
