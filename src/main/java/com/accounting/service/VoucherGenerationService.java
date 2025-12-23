package com.accounting.service;

import com.accounting.entity.AccountingSubject;
import com.accounting.entity.JournalEntry;
import com.accounting.entity.JournalEntryDetail;
import com.accounting.entity.TaxHandling;
import com.accounting.mapper.AccountingSubjectMapper;
import com.accounting.service.AccountingSubjectService;
import com.accounting.service.JournalEntryService;
import com.accounting.service.TaxHandlingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 凭证生成服务
 * 根据业务单据自动生成凭证分录
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
@Service
public class VoucherGenerationService {

    @Autowired
    private JournalEntryService journalEntryService;
    
    @Autowired
    private AccountingSubjectMapper accountingSubjectMapper;
    
    @Autowired
    private TaxHandlingService taxHandlingService;
    
    @Autowired
    private AccountingSubjectService accountingSubjectService;

    /**
     * 根据科目编码查找科目ID
     * 
     * @param code 科目编码
     * @return 科目ID
     */
    public Long findSubjectIdByCode(String code) {
        AccountingSubject subject = accountingSubjectMapper.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<AccountingSubject>()
                .eq(AccountingSubject::getCode, code)
                .eq(AccountingSubject::getStatus, 1)
        );
        return subject != null ? subject.getId() : null;
    }

    /**
     * 为采购订单生成凭证和税务记录
     * 借：库存商品/原材料/管理费用等（根据订单类型）
     * 借：应交税费-应交增值税（进项税额）
     * 贷：应付账款/银行存款（根据付款方式）
     * 
     * @param orderId 采购订单ID
     * @param orderAmount 订单总金额（含税）
     * @param taxAmount 税额（进项税）
     * @param paymentMethod 付款方式（1-现金，2-银行转账，3-应付账款）
     * @param paymentAccountId 付款账户科目ID
     * @param expenseSubjectId 费用科目ID（库存商品、原材料等）
     * @param supplierName 供应商名称
     * @return 生成的凭证ID和税务记录ID的数组 [凭证ID, 税务记录ID]
     */
    public Long[] generatePurchaseVoucher(Long orderId, BigDecimal orderAmount, BigDecimal taxAmount,
                                        Integer paymentMethod, Long paymentAccountId,
                                        Long expenseSubjectId, String supplierName) {
        BigDecimal amountWithoutTax = orderAmount.subtract(taxAmount != null ? taxAmount : BigDecimal.ZERO);
        
        JournalEntry entry = new JournalEntry();
        entry.setVoucherDate(new Date());
        entry.setSummary("采购订单：" + supplierName);
        entry.setBusinessType("PURCHASE");
        entry.setBusinessId(orderId);
        entry.setStatus(0); // 未过账
        
        List<JournalEntryDetail> details = new ArrayList<>();
        
        // 借方1：费用科目（库存商品、原材料等）- 不含税金额
        JournalEntryDetail expenseDebit = new JournalEntryDetail();
        expenseDebit.setSubjectId(expenseSubjectId);
        expenseDebit.setDebitAmount(amountWithoutTax);
        expenseDebit.setCreditAmount(BigDecimal.ZERO);
        expenseDebit.setRemark("采购商品（不含税）");
        details.add(expenseDebit);
        
        // 借方2：应交税费-应交增值税（进项税额）- 如果有税额
        Long taxRecordId = null;
        if (taxAmount != null && taxAmount.compareTo(BigDecimal.ZERO) > 0) {
            JournalEntryDetail taxDebit = new JournalEntryDetail();
            // 优先查找绑定的进项税科目（可以通过业务类型绑定）
            Long vatInputSubjectId = null;
            // 先尝试查找绑定的TAX科目（如果有的话）
            AccountingSubject boundTaxSubject = accountingSubjectService.getByBusinessType("TAX");
            if (boundTaxSubject != null) {
                // 如果绑定的科目编码包含"进项"或"2221.01.01"，使用它
                if (boundTaxSubject.getCode().contains("2221.01.01") || 
                    boundTaxSubject.getName().contains("进项")) {
                    vatInputSubjectId = boundTaxSubject.getId();
                }
            }
            // 如果没有绑定，查找默认的进项税额科目
            if (vatInputSubjectId == null) {
                vatInputSubjectId = findSubjectIdByCode("2221.01.01"); // 进项税额
            }
            // 如果还是找不到，查找应交税费主科目
            if (vatInputSubjectId == null) {
                vatInputSubjectId = findSubjectIdByCode("2221"); // 应交税费
            }
            if (vatInputSubjectId == null) {
                throw new RuntimeException("未找到进项税额科目，请在会计科目管理中配置进项税额科目（科目编码：2221.01.01）");
            }
            taxDebit.setSubjectId(vatInputSubjectId);
            taxDebit.setDebitAmount(taxAmount);
            taxDebit.setCreditAmount(BigDecimal.ZERO);
            taxDebit.setRemark("进项税额");
            details.add(taxDebit);
            
            // 创建进项税税务记录
            taxRecordId = createTaxRecord(orderId, "PURCHASE", taxAmount, "增值税（进项税）");
        }
        
        // 贷方：根据付款方式
        JournalEntryDetail creditDetail = new JournalEntryDetail();
        if (paymentMethod == 3) {
            // 应付账款
            Long payableSubjectId = null;
            // 优先使用订单中指定的付款账户科目
            if (paymentAccountId != null) {
                payableSubjectId = paymentAccountId;
            } else {
                // 查找默认的应付账款科目
                payableSubjectId = findSubjectIdByCode("2202"); // 应付账款
            }
            if (payableSubjectId == null) {
                throw new RuntimeException("未找到应付账款科目，请在会计科目管理中配置应付账款科目（科目编码：2202）");
            }
            creditDetail.setSubjectId(payableSubjectId);
            creditDetail.setRemark("应付供应商：" + supplierName);
        } else {
            // 现金或银行存款
            if (paymentAccountId == null) {
                // 默认使用银行存款
                Long bankSubjectId = findSubjectIdByCode("1002"); // 银行存款
                if (bankSubjectId == null) {
                    throw new RuntimeException("未找到银行存款科目，请在会计科目管理中配置银行存款科目（科目编码：1002）");
                }
                creditDetail.setSubjectId(bankSubjectId);
            } else {
                creditDetail.setSubjectId(paymentAccountId);
            }
            creditDetail.setRemark(paymentMethod == 1 ? "现金支付" : "银行转账");
        }
        creditDetail.setDebitAmount(BigDecimal.ZERO);
        creditDetail.setCreditAmount(orderAmount);
        details.add(creditDetail);
        
        entry.setDetails(details);
        entry.setTotalDebit(orderAmount);
        entry.setTotalCredit(orderAmount);
        
        JournalEntry saved = journalEntryService.saveEntry(entry);
        
        // 如果创建了税务记录，更新税务记录的凭证ID
        if (taxRecordId != null) {
            updateTaxRecordVoucherId(taxRecordId, saved.getId());
        }
        
        return new Long[]{saved.getId(), taxRecordId};
    }

    /**
     * 为销售订单生成凭证和税务记录
     * 借：应收账款/银行存款（根据收款方式）
     * 贷：主营业务收入（不含税）
     * 贷：应交税费-应交增值税（销项税额）
     * 
     * @param orderId 销售订单ID
     * @param orderAmount 订单总金额（含税）
     * @param taxAmount 税额（销项税）
     * @param paymentMethod 收款方式（1-现金，2-银行转账，3-应收账款）
     * @param paymentAccountId 收款账户科目ID
     * @param customerName 客户名称
     * @return 生成的凭证ID和税务记录ID的数组 [凭证ID, 税务记录ID]
     */
    public Long[] generateSalesVoucher(Long orderId, BigDecimal orderAmount, BigDecimal taxAmount,
                                    Integer paymentMethod, Long paymentAccountId,
                                    String customerName) {
        BigDecimal amountWithoutTax = orderAmount.subtract(taxAmount != null ? taxAmount : BigDecimal.ZERO);
        
        JournalEntry entry = new JournalEntry();
        entry.setVoucherDate(new Date());
        entry.setSummary("销售订单：" + customerName);
        entry.setBusinessType("SALES");
        entry.setBusinessId(orderId);
        entry.setStatus(0); // 未过账
        
        List<JournalEntryDetail> details = new ArrayList<>();
        
        // 借方：根据收款方式
        JournalEntryDetail debitDetail = new JournalEntryDetail();
        if (paymentMethod == 3) {
            // 应收账款
            Long receivableSubjectId = findSubjectIdByCode("1122"); // 应收账款
            if (receivableSubjectId == null) {
                throw new RuntimeException("未找到应收账款科目，请先配置会计科目");
            }
            debitDetail.setSubjectId(receivableSubjectId);
            debitDetail.setRemark("应收客户：" + customerName);
        } else {
            // 现金或银行存款
            if (paymentAccountId == null) {
                Long bankSubjectId = findSubjectIdByCode("1002"); // 银行存款
                if (bankSubjectId == null) {
                    throw new RuntimeException("未找到银行存款科目，请先配置会计科目");
                }
                debitDetail.setSubjectId(bankSubjectId);
            } else {
                debitDetail.setSubjectId(paymentAccountId);
            }
            debitDetail.setRemark(paymentMethod == 1 ? "现金收款" : "银行收款");
        }
        debitDetail.setDebitAmount(orderAmount);
        debitDetail.setCreditAmount(BigDecimal.ZERO);
        details.add(debitDetail);
        
        // 贷方1：主营业务收入（不含税）- 优先使用绑定的科目
        JournalEntryDetail revenueCredit = new JournalEntryDetail();
        AccountingSubject boundSubject = accountingSubjectService.getByBusinessType("SALES");
        Long revenueSubjectId = null;
        if (boundSubject != null) {
            revenueSubjectId = boundSubject.getId();
        } else {
            revenueSubjectId = findSubjectIdByCode("6001"); // 主营业务收入
            if (revenueSubjectId == null) {
                throw new RuntimeException("未找到销售单绑定的科目，请在会计科目管理中绑定销售单科目");
            }
        }
        revenueCredit.setSubjectId(revenueSubjectId);
        revenueCredit.setDebitAmount(BigDecimal.ZERO);
        revenueCredit.setCreditAmount(amountWithoutTax);
        revenueCredit.setRemark("销售商品收入（不含税）");
        details.add(revenueCredit);
        
        // 贷方2：应交税费-应交增值税（销项税额）- 如果有税额
        Long taxRecordId = null;
        if (taxAmount != null && taxAmount.compareTo(BigDecimal.ZERO) > 0) {
            JournalEntryDetail taxCredit = new JournalEntryDetail();
            // 优先查找绑定的销项税科目
            Long vatOutputSubjectId = null;
            // 先尝试查找绑定的TAX科目（如果有的话）
            AccountingSubject boundTaxSubject = accountingSubjectService.getByBusinessType("TAX");
            if (boundTaxSubject != null) {
                // 如果绑定的科目编码包含"销项"或"2221.01.02"，使用它
                if (boundTaxSubject.getCode().contains("2221.01.02") || 
                    boundTaxSubject.getName().contains("销项")) {
                    vatOutputSubjectId = boundTaxSubject.getId();
                }
            }
            // 如果没有绑定，查找默认的销项税额科目
            if (vatOutputSubjectId == null) {
                vatOutputSubjectId = findSubjectIdByCode("2221.01.02"); // 销项税额
            }
            // 如果还是找不到，查找应交税费主科目
            if (vatOutputSubjectId == null) {
                vatOutputSubjectId = findSubjectIdByCode("2221"); // 应交税费
            }
            if (vatOutputSubjectId == null) {
                throw new RuntimeException("未找到销项税额科目，请在会计科目管理中配置销项税额科目（科目编码：2221.01.02）");
            }
            taxCredit.setSubjectId(vatOutputSubjectId);
            taxCredit.setDebitAmount(BigDecimal.ZERO);
            taxCredit.setCreditAmount(taxAmount);
            taxCredit.setRemark("销项税额");
            details.add(taxCredit);
            
            // 创建销项税税务记录
            taxRecordId = createTaxRecord(orderId, "SALES", taxAmount, "增值税（销项税）");
        }
        
        entry.setDetails(details);
        entry.setTotalDebit(orderAmount);
        entry.setTotalCredit(orderAmount);
        
        JournalEntry saved = journalEntryService.saveEntry(entry);
        
        // 如果创建了税务记录，更新税务记录的凭证ID
        if (taxRecordId != null) {
            updateTaxRecordVoucherId(taxRecordId, saved.getId());
        }
        
        return new Long[]{saved.getId(), taxRecordId};
    }

    /**
     * 为员工工资生成凭证和税务记录
     * 借：应付职工薪酬
     * 贷：银行存款、应交税费-个人所得税
     * 
     * @param employeeId 员工ID
     * @param employeeName 员工姓名
     * @param baseSalary 基本工资
     * @param paymentAccountId 付款账户科目ID
     * @return 生成的凭证ID和税务记录ID的数组 [凭证ID, 税务记录ID]，如果没有税务则税务记录ID为null
     */
    public Long[] generateSalaryVoucher(Long employeeId, String employeeName, 
                                      BigDecimal baseSalary, Long paymentAccountId) {
        // 计算个人所得税（简化计算，实际应按照税法规定）
        BigDecimal taxAmount = calculatePersonalIncomeTax(baseSalary);
        BigDecimal netSalary = baseSalary.subtract(taxAmount);
        
        JournalEntry entry = new JournalEntry();
        entry.setVoucherDate(new Date());
        entry.setSummary("发放工资：" + employeeName);
        entry.setBusinessType("SALARY");
        entry.setBusinessId(employeeId);
        entry.setStatus(0); // 未过账
        
        List<JournalEntryDetail> details = new ArrayList<>();
        
        // 借方：应付职工薪酬
        JournalEntryDetail salaryDebit = new JournalEntryDetail();
        Long salarySubjectId = findSubjectIdByCode("2211"); // 应付职工薪酬
        if (salarySubjectId == null) {
            throw new RuntimeException("未找到应付职工薪酬科目，请先配置会计科目");
        }
        salaryDebit.setSubjectId(salarySubjectId);
        salaryDebit.setDebitAmount(baseSalary);
        salaryDebit.setCreditAmount(BigDecimal.ZERO);
        salaryDebit.setRemark("应付工资");
        details.add(salaryDebit);
        
        // 贷方1：银行存款（实发工资）
        JournalEntryDetail bankCredit = new JournalEntryDetail();
        if (paymentAccountId == null) {
            Long bankSubjectId = findSubjectIdByCode("1002"); // 银行存款
            if (bankSubjectId == null) {
                throw new RuntimeException("未找到银行存款科目，请先配置会计科目");
            }
            bankCredit.setSubjectId(bankSubjectId);
        } else {
            bankCredit.setSubjectId(paymentAccountId);
        }
        bankCredit.setDebitAmount(BigDecimal.ZERO);
        bankCredit.setCreditAmount(netSalary);
        bankCredit.setRemark("实发工资");
        details.add(bankCredit);
        
        Long taxRecordId = null;
        // 贷方2：应交税费-个人所得税（如果有税）
        if (taxAmount.compareTo(BigDecimal.ZERO) > 0) {
            JournalEntryDetail taxCredit = new JournalEntryDetail();
            Long taxSubjectId = findSubjectIdByCode("2221"); // 应交税费
            if (taxSubjectId == null) {
                throw new RuntimeException("未找到应交税费科目，请先配置会计科目");
            }
            taxCredit.setSubjectId(taxSubjectId);
            taxCredit.setDebitAmount(BigDecimal.ZERO);
            taxCredit.setCreditAmount(taxAmount);
            taxCredit.setRemark("代扣个人所得税");
            details.add(taxCredit);
            
            // 自动创建税务记录
            taxRecordId = createTaxRecord(employeeId, "SALARY", taxAmount, "个人所得税");
        }
        
        entry.setDetails(details);
        entry.setTotalDebit(baseSalary);
        entry.setTotalCredit(baseSalary);
        
        JournalEntry saved = journalEntryService.saveEntry(entry);
        
        // 如果创建了税务记录，更新税务记录的凭证ID
        if (taxRecordId != null) {
            updateTaxRecordVoucherId(taxRecordId, saved.getId());
        }
        
        return new Long[]{saved.getId(), taxRecordId};
    }
    
    /**
     * 创建税务记录（系统自动生成）
     * 
     * @param businessId 业务ID
     * @param businessType 业务类型
     * @param taxAmount 税额
     * @param taxType 税种
     * @return 税务记录ID
     */
    private Long createTaxRecord(Long businessId, String businessType, BigDecimal taxAmount, String taxType) {
        TaxHandling tax = new TaxHandling();
        tax.setBusinessId(businessId);
        tax.setBusinessType(businessType);
        tax.setTaxAmount(taxAmount);
        tax.setTaxType(taxType);
        tax.setTaxPeriod(new SimpleDateFormat("yyyy-MM").format(new Date()));
        tax.setStatus(0); // 未申报
        tax.setAutoGenerated(true); // 标记为系统自动生成
        
        TaxHandling saved = taxHandlingService.saveTaxHandling(tax);
        return saved.getId();
    }
    
    /**
     * 更新税务记录的凭证ID
     */
    private void updateTaxRecordVoucherId(Long taxRecordId, Long voucherId) {
        TaxHandling tax = taxHandlingService.getById(taxRecordId);
        if (tax != null) {
            tax.setJournalEntryId(voucherId);
            taxHandlingService.updateTaxHandling(tax);
        }
    }

    /**
     * 为员工费用报销生成凭证
     * 借：管理费用/销售费用等
     * 贷：银行存款
     * 
     * @param expenseId 费用ID
     * @param expenseAmount 费用金额
     * @param expenseSubjectId 费用科目ID（管理费用、销售费用等）
     * @param paymentAccountId 付款账户科目ID
     * @param employeeName 员工姓名
     * @return 生成的凭证ID
     */
    public Long generateExpenseVoucher(Long expenseId, BigDecimal expenseAmount,
                                      Long expenseSubjectId, Long paymentAccountId,
                                      String employeeName) {
        JournalEntry entry = new JournalEntry();
        entry.setVoucherDate(new Date());
        entry.setSummary("费用报销：" + employeeName);
        entry.setBusinessType("EXPENSE");
        entry.setBusinessId(expenseId);
        entry.setStatus(0); // 未过账
        
        List<JournalEntryDetail> details = new ArrayList<>();
        
        // 借方：费用科目 - 优先使用绑定的科目
        JournalEntryDetail expenseDebit = new JournalEntryDetail();
        Long finalExpenseSubjectId = expenseSubjectId;
        if (finalExpenseSubjectId == null) {
            // 优先使用绑定的科目
            AccountingSubject boundSubject = accountingSubjectService.getByBusinessType("EXPENSE");
            if (boundSubject != null) {
                finalExpenseSubjectId = boundSubject.getId();
            } else {
                // 默认使用管理费用
                Long defaultExpenseId = findSubjectIdByCode("6602"); // 管理费用
                if (defaultExpenseId == null) {
                    throw new RuntimeException("未找到员工费用绑定的科目，请在会计科目管理中绑定员工费用科目");
                }
                finalExpenseSubjectId = defaultExpenseId;
            }
        }
        expenseDebit.setSubjectId(finalExpenseSubjectId);
        expenseDebit.setDebitAmount(expenseAmount);
        expenseDebit.setCreditAmount(BigDecimal.ZERO);
        expenseDebit.setRemark("费用报销");
        details.add(expenseDebit);
        
        // 贷方：银行存款
        JournalEntryDetail bankCredit = new JournalEntryDetail();
        if (paymentAccountId == null) {
            Long bankSubjectId = findSubjectIdByCode("1002"); // 银行存款
            if (bankSubjectId == null) {
                throw new RuntimeException("未找到银行存款科目，请先配置会计科目");
            }
            bankCredit.setSubjectId(bankSubjectId);
        } else {
            bankCredit.setSubjectId(paymentAccountId);
        }
        bankCredit.setDebitAmount(BigDecimal.ZERO);
        bankCredit.setCreditAmount(expenseAmount);
        bankCredit.setRemark("报销支付");
        details.add(bankCredit);
        
        entry.setDetails(details);
        entry.setTotalDebit(expenseAmount);
        entry.setTotalCredit(expenseAmount);
        
        JournalEntry saved = journalEntryService.saveEntry(entry);
        return saved.getId();
    }

    /**
     * 计算个人所得税（简化版）
     * 实际应按照税法规定的累进税率计算
     * 
     * @param salary 工资
     * @return 税额
     */
    private BigDecimal calculatePersonalIncomeTax(BigDecimal salary) {
        // 起征点5000元
        BigDecimal threshold = new BigDecimal("5000");
        if (salary.compareTo(threshold) <= 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal taxableIncome = salary.subtract(threshold);
        BigDecimal tax = BigDecimal.ZERO;
        
        // 简化税率计算（实际应按累进税率）
        if (taxableIncome.compareTo(new BigDecimal("3000")) <= 0) {
            tax = taxableIncome.multiply(new BigDecimal("0.03"));
        } else if (taxableIncome.compareTo(new BigDecimal("12000")) <= 0) {
            tax = new BigDecimal("90").add(taxableIncome.subtract(new BigDecimal("3000")).multiply(new BigDecimal("0.1")));
        } else if (taxableIncome.compareTo(new BigDecimal("25000")) <= 0) {
            tax = new BigDecimal("990").add(taxableIncome.subtract(new BigDecimal("12000")).multiply(new BigDecimal("0.2")));
        } else {
            tax = new BigDecimal("3590").add(taxableIncome.subtract(new BigDecimal("25000")).multiply(new BigDecimal("0.25")));
        }
        
        return tax.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}

