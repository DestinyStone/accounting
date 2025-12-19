package com.accounting.service.impl;

import com.accounting.entity.Enterprise;
import com.accounting.mapper.EnterpriseMapper;
import com.accounting.service.EnterpriseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

/**
 * 企业服务实现类
 */
@Service
public class EnterpriseServiceImpl extends ServiceImpl<EnterpriseMapper, Enterprise> implements EnterpriseService {

    @Autowired
    private EnterpriseMapper enterpriseMapper;

    @Override
    public boolean initEnterprise(Enterprise enterprise) {
        // 检查是否已存在企业信息
        if (isInitialized()) {
            return false;
        }


        // 保存企业信息
        return enterpriseMapper.insert(enterprise) > 0;
    }

    @Override
    public Enterprise getEnterpriseInfo() {
        // 查询第一个企业信息（系统只允许一个企业）
        List<Enterprise> enterprises = enterpriseMapper.selectList(null);
        return enterprises.isEmpty() ? null : enterprises.get(0);
    }

    @Override
    public boolean isInitialized() {
        // 查询是否存在企业信息
        List<Enterprise> enterprises = enterpriseMapper.selectList(null);
        return !enterprises.isEmpty();
    }
}
