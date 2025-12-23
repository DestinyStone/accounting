package com.accounting.service.impl;

import com.accounting.entity.Employee;
import com.accounting.mapper.EmployeeMapper;
import com.accounting.service.EmployeeSalaryService;
import com.accounting.service.VoucherGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 员工工资发放服务实现类
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
@Service
public class EmployeeSalaryServiceImpl implements EmployeeSalaryService {

    @Autowired
    private EmployeeMapper employeeMapper;
    
    @Autowired
    private VoucherGenerationService voucherGenerationService;

    /**
     * 发放员工工资
     * 自动计算个人所得税并生成凭证和税务记录
     * 
     * @param employeeId 员工ID
     * @param salary 工资金额
     * @param paymentAccountId 付款账户科目ID
     * @return 生成的凭证ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long paySalary(Long employeeId, BigDecimal salary, Long paymentAccountId) {
        // 查询员工信息
        Employee employee = employeeMapper.selectById(employeeId);
        if (employee == null) {
            throw new RuntimeException("员工不存在");
        }
        
        // 使用员工的基本工资，如果没有则使用传入的工资
        BigDecimal actualSalary = employee.getBaseSalary() != null && employee.getBaseSalary().compareTo(BigDecimal.ZERO) > 0
            ? employee.getBaseSalary() : salary;
        
        // 生成凭证和税务记录
        Long[] result = voucherGenerationService.generateSalaryVoucher(
            employeeId,
            employee.getEmployeeName(),
            actualSalary,
            paymentAccountId
        );
        
        return result[0]; // 返回凭证ID
    }

    /**
     * 批量发放工资
     * 
     * @param employeeIds 员工ID列表
     * @param salary 工资金额（统一金额）
     * @param paymentAccountId 付款账户科目ID
     * @return 成功数量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchPaySalary(List<Long> employeeIds, BigDecimal salary, Long paymentAccountId) {
        int successCount = 0;
        for (Long employeeId : employeeIds) {
            try {
                paySalary(employeeId, salary, paymentAccountId);
                successCount++;
            } catch (Exception e) {
                // 记录错误但继续处理其他员工
                System.err.println("发放员工" + employeeId + "工资失败：" + e.getMessage());
            }
        }
        return successCount;
    }
}



