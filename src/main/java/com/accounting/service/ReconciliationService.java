package com.accounting.service;

import com.accounting.entity.Reconciliation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 对账服务接口
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
public interface ReconciliationService extends IService<Reconciliation> {
    // 历史遗留接口：create/update/complete 暂保留以兼容调用，目前对账走 autoReconcile 直接生成已对账记录
    Reconciliation createReconciliation(Reconciliation reconciliation);
    Reconciliation updateReconciliation(Reconciliation reconciliation);
    boolean completeReconciliation(Long id);
    /**
     * 自动计算账户余额
     * 根据已过账的凭证明细计算指定科目的余额
     * 
     * @param subjectId 科目ID
     * @param endDate 截止日期（可选，如果为null则计算到当前日期）
     * @return 账户余额（借方余额为正，贷方余额为负）
     */
    BigDecimal calculateAccountBalance(Long subjectId, Date endDate);
    
    /**
     * 自动对账
     * 根据已过账的凭证自动计算账面余额，结合对账单余额生成一条对账记录
     * 
     * @param subjectId 科目ID
     * @param reconciliationDate 对账日期
     * @param statementBalance 对账单余额
     * @param remark 对账说明
     * @param userId 对账人
     * @return 对账记录
     */
    Reconciliation autoReconcile(Long subjectId, Date reconciliationDate,
                                 BigDecimal statementBalance, String remark, Long userId);
    
    /**
     * 获取对账详情（包含明细）
     * 
     * @param id 对账记录ID
     * @return 对账详情（包含期初余额、期末余额、明细等）
     */
    Map<String, Object> getReconciliationDetail(Long id);
}
