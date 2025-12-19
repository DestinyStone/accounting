package com.accounting.service;

import com.accounting.entity.Posting;
import com.baomidou.mybatisplus.extension.service.IService;

public interface PostingService extends IService<Posting> {
    boolean postJournalEntry(Long entryId, String userId);
    boolean cancelPosting(Long entryId);
    Posting getByEntryId(Long entryId);
}
