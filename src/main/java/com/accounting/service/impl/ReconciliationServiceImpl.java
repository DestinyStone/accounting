package com.accounting.service.impl;

import com.accounting.entity.Reconciliation;
import com.accounting.mapper.ReconciliationMapper;
import com.accounting.service.ReconciliationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReconciliationServiceImpl extends ServiceImpl<ReconciliationMapper, Reconciliation> implements ReconciliationService {

    @Autowired
    private ReconciliationMapper reconciliationMapper;

    @Override
    public Reconciliation createReconciliation(Reconciliation reconciliation) {
        reconciliationMapper.insert(reconciliation);
        return reconciliation;
    }

    @Override
    public Reconciliation updateReconciliation(Reconciliation reconciliation) {
        reconciliationMapper.updateById(reconciliation);
        return reconciliation;
    }

    @Override
    public boolean completeReconciliation(Long id) {
        Reconciliation reconciliation = new Reconciliation();
        reconciliation.setId(id);
        reconciliation.setStatus(1); // 已完成
        return reconciliationMapper.updateById(reconciliation) > 0;
    }
}