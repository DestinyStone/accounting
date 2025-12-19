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
 */
@RestController
@RequestMapping(Api.path + "/accounting-subject")
public class AccountingSubjectController {

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
}
