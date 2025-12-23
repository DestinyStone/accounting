package com.accounting.service.impl;

import com.accounting.entity.AccountingSubject;
import com.accounting.mapper.AccountingSubjectMapper;
import com.accounting.service.AccountingSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会计科目服务实现类
 */
@Service
public class AccountingSubjectServiceImpl extends ServiceImpl<AccountingSubjectMapper, AccountingSubject> implements AccountingSubjectService {

    @Autowired
    private AccountingSubjectMapper accountingSubjectMapper;

    @Override
    public List<AccountingSubject> getSubjectTree() {
        // 获取所有科目
        List<AccountingSubject> allSubjects = list();
        return allSubjects;
    }

    @Override
    public List<AccountingSubject> getChildrenByParentId(Long parentId) {
        return accountingSubjectMapper.selectByParentId(parentId);
    }

    @Override
    public boolean addSubject(AccountingSubject subject) {
        return save(subject);
    }

    @Override
    public boolean updateSubject(AccountingSubject subject) {
        return updateById(subject);
    }

    @Override
    public boolean deleteSubject(Long id) {
        return removeById(id);
    }

    @Override
    public boolean enableSubject(Long id, Boolean enabled) {
        AccountingSubject subject = getById(id);
        if (subject != null) {
            subject.setStatus(enabled ? 1 : 0);
            return updateById(subject);
        }
        return false;
    }

    @Override
    public List<AccountingSubject> getEnabledLeafSubjects() {
        return accountingSubjectMapper.selectEnabledLeafSubjects();
    }

    @Override
    public boolean bindBusinessType(Long id, String businessType) {
        // 检查是否已有其他科目绑定到该业务类型
        AccountingSubject existingSubject = getByBusinessType(businessType);
        if (existingSubject != null && !existingSubject.getId().equals(id)) {
            throw new RuntimeException("该业务类型已绑定到科目：" + existingSubject.getCode() + "-" + existingSubject.getName());
        }
        
        // 更新科目绑定
        AccountingSubject subject = getById(id);
        if (subject != null) {
            subject.setBusinessType(businessType);
            return updateById(subject);
        }
        return false;
    }

    @Override
    public boolean unbindBusinessType(Long id) {
        AccountingSubject subject = getById(id);
        if (subject != null) {
            subject.setBusinessType(null);
            return updateById(subject);
        }
        return false;
    }

    @Override
    public AccountingSubject getByBusinessType(String businessType) {
        return accountingSubjectMapper.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<AccountingSubject>()
                .eq(AccountingSubject::getBusinessType, businessType)
                .eq(AccountingSubject::getStatus, 1)
        );
    }
}
