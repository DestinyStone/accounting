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
}
