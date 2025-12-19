package com.accounting.service;

import com.accounting.entity.Enterprise;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 企业服务接口
 */
public interface EnterpriseService extends IService<Enterprise> {

    /**
     * 初始化企业信息
     * @param enterprise 企业信息
     * @return 初始化结果
     */
    boolean initEnterprise(Enterprise enterprise);

    /**
     * 获取企业信息
     * @return 企业信息
     */
    Enterprise getEnterpriseInfo();

    /**
     * 判断企业是否已初始化
     * @return 是否已初始化
     */
    boolean isInitialized();
}
