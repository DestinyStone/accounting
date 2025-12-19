package com.accounting.controller;

import com.accounting.common.Api;
import com.accounting.common.Result;
import com.accounting.entity.Enterprise;
import com.accounting.service.EnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 企业信息控制器
 */
@RestController
@RequestMapping(Api.path + "/enterprise")
public class EnterpriseController {

    @Autowired
    private EnterpriseService enterpriseService;

    /**
     * 初始化企业信息
     */
    @PostMapping("/init")
    public Result<Boolean> init(@RequestBody Enterprise enterprise) {
        Enterprise enterpriseInfo = enterpriseService.getEnterpriseInfo();
        if (enterpriseInfo == null) {
            enterpriseService.initEnterprise(enterprise);
        }else {
            enterprise.setId(enterpriseInfo.getId());
            enterpriseService.updateById(enterprise);
        }
        return Result.success(true);
    }

    /**
     * 获取企业信息
     */
    @GetMapping("/info")
    public Result<Enterprise> getEnterpriseInfo() {
        try {
            Enterprise enterprise = enterpriseService.getEnterpriseInfo();
            if (enterprise != null) {
                return Result.success(enterprise);
            } else {
                return Result.error("企业信息不存在");
            }
        } catch (Exception e) {
            return Result.error("获取企业信息失败：" + e.getMessage());
        }
    }

    /**
     * 检查企业是否已初始化
     */
    @GetMapping("/check")
    public Result<Boolean> checkInitialized() {
        try {
            boolean initialized = enterpriseService.isInitialized();
            return Result.success(initialized);
        } catch (Exception e) {
            return Result.error("检查企业状态失败：" + e.getMessage());
        }
    }
}
