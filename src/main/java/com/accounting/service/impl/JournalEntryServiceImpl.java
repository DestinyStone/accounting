package com.accounting.service.impl;

import com.accounting.entity.JournalEntry;
import com.accounting.entity.JournalEntryDetail;
import com.accounting.mapper.AccountingSubjectMapper;
import com.accounting.mapper.JournalEntryDetailMapper;
import com.accounting.mapper.JournalEntryMapper;
import com.accounting.service.JournalEntryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JournalEntryServiceImpl extends ServiceImpl<JournalEntryMapper, JournalEntry> implements JournalEntryService {

    @Autowired
    private JournalEntryMapper journalEntryMapper;
    
    @Autowired
    private JournalEntryDetailMapper journalEntryDetailMapper;
    
    @Autowired
    private AccountingSubjectMapper accountingSubjectMapper;

    @Override
    @Transactional
    public JournalEntry saveEntry(JournalEntry journalEntry) {
        // 生成凭证编号
        String voucherDate = new SimpleDateFormat("yyyy-MM-dd").format(journalEntry.getVoucherDate());
        journalEntry.setVoucherNo(generateVoucherNo(voucherDate));
        
        // 保存主表
        journalEntryMapper.insert(journalEntry);
        
        // 保存明细
        if (journalEntry.getDetails() != null && !journalEntry.getDetails().isEmpty()) {
            for (JournalEntryDetail detail : journalEntry.getDetails()) {
                detail.setEntryId(journalEntry.getId());
            }
            journalEntryDetailMapper.batchInsert(journalEntry.getDetails());
        }
        
        return journalEntry;
    }

    @Override
    @Transactional
    public JournalEntry updateEntry(JournalEntry journalEntry) {
        // 更新主表
        journalEntryMapper.updateById(journalEntry);
        
        // 删除旧明细
        journalEntryDetailMapper.deleteByEntryId(journalEntry.getId());
        
        // 保存新明细
        if (journalEntry.getDetails() != null && !journalEntry.getDetails().isEmpty()) {
            for (JournalEntryDetail detail : journalEntry.getDetails()) {
                detail.setEntryId(journalEntry.getId());
            }
            journalEntryDetailMapper.batchInsert(journalEntry.getDetails());
        }
        
        return journalEntry;
    }

    @Override
    public JournalEntry getByIdWithDetails(Long id) {
        return journalEntryMapper.selectByIdWithDetails(id);
    }

    @Override
    public List<JournalEntry> queryWithDetails(Map<String, Object> params) {
        return journalEntryMapper.selectWithDetails(params);
    }

    @Override
    @Transactional
    public boolean deleteEntry(Long id) {
        // 删除明细
        journalEntryDetailMapper.deleteByEntryId(id);
        // 删除主表
        return journalEntryMapper.deleteById(id) > 0;
    }

    @Override
    public String generateVoucherNo(String date) {
        // 获取当前日期的最大凭证号
        String maxNo = journalEntryMapper.getMaxVoucherNoByDate(date);
        
        // 格式化日期为yyMMdd
        String dateStr = date.substring(2, 4) + date.substring(5, 7) + date.substring(8, 10);
        
        if (maxNo == null) {
            return "V" + dateStr + "001";
        }
        
        // 提取序号部分并加1
        int seq = Integer.parseInt(maxNo.substring(9)) + 1;
        return "V" + dateStr + String.format("%03d", seq);
    }
    
    @Override
    public List<Map<String, Object>> getDetailsWithSubject(Long entryId) {
        List<JournalEntryDetail> details = journalEntryDetailMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<JournalEntryDetail>()
                .eq(JournalEntryDetail::getEntryId, entryId)
        );
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (JournalEntryDetail detail : details) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", detail.getId());
            map.put("entryId", detail.getEntryId());
            map.put("subjectId", detail.getSubjectId());
            map.put("debitAmount", detail.getDebitAmount());
            map.put("creditAmount", detail.getCreditAmount());
            map.put("remark", detail.getRemark());
            
            // 查询科目信息
            if (detail.getSubjectId() != null) {
                com.accounting.entity.AccountingSubject subject = accountingSubjectMapper.selectById(detail.getSubjectId());
                if (subject != null) {
                    map.put("subjectCode", subject.getCode());
                    map.put("subjectName", subject.getName());
                } else {
                    map.put("subjectCode", "");
                    map.put("subjectName", "");
                }
            } else {
                map.put("subjectCode", "");
                map.put("subjectName", "");
            }
            
            result.add(map);
        }
        
        return result;
    }
}