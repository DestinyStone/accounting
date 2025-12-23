package com.accounting.service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 员工工资发放服务接口
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
public interface EmployeeSalaryService {
    
    /**
     * 发放员工工资
     * 自动计算个人所得税并生成凭证和税务记录
     * 
     * @param employeeId 员工ID
     * @param salary 工资金额
     * @param paymentAccountId 付款账户科目ID
     * @return 生成的凭证ID
     */
    Long paySalary(Long employeeId, BigDecimal salary, Long paymentAccountId);
    
    /**
     * 批量发放工资
     * 
     * @param employeeIds 员工ID列表
     * @param salary 工资金额（统一金额）
     * @param paymentAccountId 付款账户科目ID
     * @return 成功数量
     */
    int batchPaySalary(List<Long> employeeIds, BigDecimal salary, Long paymentAccountId);
}



