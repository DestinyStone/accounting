package com.accounting.service.impl;

import com.accounting.entity.AccountingSubject;
import com.accounting.entity.JournalEntry;
import com.accounting.entity.JournalEntryDetail;
import com.accounting.entity.Reconciliation;
import com.accounting.mapper.AccountingSubjectMapper;
import com.accounting.mapper.JournalEntryDetailMapper;
import com.accounting.mapper.JournalEntryMapper;
import com.accounting.mapper.ReconciliationMapper;
import com.accounting.service.ReconciliationService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对账服务实现类
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
@Service
public class ReconciliationServiceImpl extends ServiceImpl<ReconciliationMapper, Reconciliation> implements ReconciliationService {

    @Autowired
    private ReconciliationMapper reconciliationMapper;
    
    @Autowired
    private JournalEntryMapper journalEntryMapper;
    
    @Autowired
    private JournalEntryDetailMapper journalEntryDetailMapper;
    
    @Autowired
    private AccountingSubjectMapper accountingSubjectMapper;

    @Override
    @Transactional
    public Reconciliation createReconciliation(Reconciliation reconciliation) {
        // 兼容旧接口：如果未设置账面余额，则自动计算；对账单余额和差额由调用方保证
        if (reconciliation.getBookBalance() == null
                && reconciliation.getReconciliationDate() != null
                && reconciliation.getAccountId() != null) {
            BigDecimal bookBalance = calculateAccountBalance(reconciliation.getAccountId(), reconciliation.getReconciliationDate());
            reconciliation.setBookBalance(bookBalance);
        }
        if (reconciliation.getStatementBalance() != null && reconciliation.getBookBalance() != null) {
            reconciliation.setDifference(reconciliation.getStatementBalance().subtract(reconciliation.getBookBalance()));
        }
        if (reconciliation.getStatus() == null) {
            reconciliation.setStatus(0); // 未对账
        }
        reconciliation.setCreatedAt(new Date());
        reconciliationMapper.insert(reconciliation);
        return reconciliation;
    }

    @Override
    @Transactional
    public Reconciliation updateReconciliation(Reconciliation reconciliation) {
        reconciliationMapper.updateById(reconciliation);
        return reconciliation;
    }

    @Override
    @Transactional
    public boolean completeReconciliation(Long id) {
        Reconciliation reconciliation = new Reconciliation();
        reconciliation.setId(id);
        reconciliation.setStatus(1); // 已对账
        return reconciliationMapper.updateById(reconciliation) > 0;
    }
    
    @Override
    public BigDecimal calculateAccountBalance(Long subjectId, Date endDate) {
        // 查询科目信息，获取余额方向
        AccountingSubject subject = accountingSubjectMapper.selectById(subjectId);
        if (subject == null) {
            throw new RuntimeException("科目不存在");
        }
        
        // 查询已过账的凭证明细（只统计已过账的凭证）
        List<JournalEntry> entries = journalEntryMapper.selectList(
            new LambdaQueryWrapper<JournalEntry>()
                .eq(JournalEntry::getStatus, 1) // 只统计已过账的凭证
                .le(endDate != null, JournalEntry::getVoucherDate, endDate)
        );
        
        BigDecimal debitTotal = BigDecimal.ZERO;
        BigDecimal creditTotal = BigDecimal.ZERO;
        
        // 直接查询该科目的所有明细，提高效率
        List<JournalEntryDetail> allDetails = journalEntryDetailMapper.selectList(
            new LambdaQueryWrapper<JournalEntryDetail>()
                .eq(JournalEntryDetail::getSubjectId, subjectId)
        );
        
        // 过滤出已过账凭证的明细
        for (JournalEntryDetail detail : allDetails) {
            JournalEntry entry = journalEntryMapper.selectById(detail.getEntryId());
            if (entry != null && entry.getStatus() == 1) {
                // 检查日期
                if (endDate == null || entry.getVoucherDate().compareTo(endDate) <= 0) {
                    debitTotal = debitTotal.add(detail.getDebitAmount() != null ? detail.getDebitAmount() : BigDecimal.ZERO);
                    creditTotal = creditTotal.add(detail.getCreditAmount() != null ? detail.getCreditAmount() : BigDecimal.ZERO);
                }
            }
        }
        
        // 根据余额方向计算余额
        // 余额方向1-借方：余额 = 借方 - 贷方
        // 余额方向2-贷方：余额 = 贷方 - 借方（负数表示）
        BigDecimal balance;
        if (subject.getBalanceDirection() == 1) {
            // 借方科目：余额 = 借方 - 贷方
            balance = debitTotal.subtract(creditTotal);
        } else {
            // 贷方科目：余额 = 贷方 - 借方（负数表示）
            balance = creditTotal.subtract(debitTotal);
        }
        
        return balance;
    }
    
    @Override
    @Transactional
    public Reconciliation autoReconcile(Long subjectId, Date reconciliationDate,
                                        BigDecimal statementBalance, String remark, Long userId) {
        // 计算账面余额（到对账日期的已过账凭证）
        BigDecimal bookBalance = calculateAccountBalance(subjectId, reconciliationDate);
        
        // 生成对账记录：直接记为已对账
        Reconciliation reconciliation = new Reconciliation();
        reconciliation.setAccountId(subjectId);
        reconciliation.setReconciliationDate(reconciliationDate);
        reconciliation.setBookBalance(bookBalance);
        reconciliation.setStatementBalance(statementBalance != null ? statementBalance : BigDecimal.ZERO);
        reconciliation.setDifference(reconciliation.getStatementBalance().subtract(bookBalance));
        reconciliation.setStatus(1); // 已对账
        reconciliation.setRemark(remark);
        reconciliation.setUserId(userId);
        reconciliation.setCreatedAt(new Date());
        
        reconciliationMapper.insert(reconciliation);
        return reconciliation;
    }
    
    @Override
    public Map<String, Object> getReconciliationDetail(Long id) {
        Reconciliation reconciliation = reconciliationMapper.selectById(id);
        if (reconciliation == null) {
            throw new RuntimeException("对账记录不存在");
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("reconciliation", reconciliation);
        
        // 查询科目信息
        Long subjectId = reconciliation.getAccountId();
        AccountingSubject subject = accountingSubjectMapper.selectById(subjectId);
        result.put("subject", subject);
        
        // 计算账面余额的明细信息（借方总额、贷方总额）
        BigDecimal debitTotal = BigDecimal.ZERO;
        BigDecimal creditTotal = BigDecimal.ZERO;
        
        // 查询该科目在对账日期之前的所有已过账凭证明细
        List<JournalEntryDetail> allDetails = journalEntryDetailMapper.selectList(
            new LambdaQueryWrapper<JournalEntryDetail>()
                .eq(JournalEntryDetail::getSubjectId, subjectId)
        );
        
        // 过滤出已过账凭证的明细，并统计借方和贷方总额
        List<Map<String, Object>> entryDetails = new ArrayList<>();
        for (JournalEntryDetail detail : allDetails) {
            JournalEntry entry = journalEntryMapper.selectById(detail.getEntryId());
            if (entry != null && entry.getStatus() == 1) {
                // 检查日期：只统计对账日期之前的凭证
                if (reconciliation.getReconciliationDate() == null || 
                    entry.getVoucherDate().compareTo(reconciliation.getReconciliationDate()) <= 0) {
                    BigDecimal debit = detail.getDebitAmount() != null ? detail.getDebitAmount() : BigDecimal.ZERO;
                    BigDecimal credit = detail.getCreditAmount() != null ? detail.getCreditAmount() : BigDecimal.ZERO;
                    debitTotal = debitTotal.add(debit);
                    creditTotal = creditTotal.add(credit);
                    
                    // 记录凭证明细
                    Map<String, Object> detailMap = new HashMap<>();
                    detailMap.put("entryId", entry.getId());
                    detailMap.put("voucherNo", entry.getVoucherNo());
                    detailMap.put("voucherDate", entry.getVoucherDate());
                    detailMap.put("summary", entry.getSummary());
                    detailMap.put("debitAmount", debit);
                    detailMap.put("creditAmount", credit);
                    detailMap.put("remark", detail.getRemark());
                    entryDetails.add(detailMap);
                }
            }
        }
        
        // 根据余额方向计算账面余额
        BigDecimal calculatedBalance;
        if (subject.getBalanceDirection() == 1) {
            // 借方科目：余额 = 借方 - 贷方
            calculatedBalance = debitTotal.subtract(creditTotal);
        } else {
            // 贷方科目：余额 = 贷方 - 借方
            calculatedBalance = creditTotal.subtract(debitTotal);
        }
        
        // 账面余额计算明细
        Map<String, Object> balanceDetail = new HashMap<>();
        balanceDetail.put("debitTotal", debitTotal);
        balanceDetail.put("creditTotal", creditTotal);
        balanceDetail.put("balanceDirection", subject.getBalanceDirection());
        balanceDetail.put("balanceDirectionText", subject.getBalanceDirection() == 1 ? "借方" : "贷方");
        balanceDetail.put("calculatedBalance", calculatedBalance);
        result.put("balanceDetail", balanceDetail);
        
        // 对账比对信息
        Map<String, Object> comparison = new HashMap<>();
        comparison.put("bookBalance", reconciliation.getBookBalance());
        comparison.put("statementBalance", reconciliation.getStatementBalance());
        comparison.put("difference", reconciliation.getDifference());
        result.put("comparison", comparison);
        
        // 对账期间的凭证明细
        result.put("entryDetails", entryDetails);
        
        return result;
    }
}