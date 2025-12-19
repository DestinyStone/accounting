package com.accounting.service;

import com.accounting.entity.Reconciliation;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ReconciliationService extends IService<Reconciliation> {
    Reconciliation createReconciliation(Reconciliation reconciliation);
    Reconciliation updateReconciliation(Reconciliation reconciliation);
    boolean completeReconciliation(Long id);
}
