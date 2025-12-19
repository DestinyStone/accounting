package com.accounting.service;

import com.accounting.entity.JournalEntry;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface JournalEntryService extends IService<JournalEntry> {
    JournalEntry saveEntry(JournalEntry journalEntry);
    JournalEntry updateEntry(JournalEntry journalEntry);
    JournalEntry getByIdWithDetails(Long id);
    List<JournalEntry> queryWithDetails(Map<String, Object> params);
    boolean deleteEntry(Long id);
    String generateVoucherNo(String date);
}
