package com.accounting.service.impl;

import com.accounting.entity.Posting;
import com.accounting.mapper.JournalEntryMapper;
import com.accounting.mapper.PostingMapper;
import com.accounting.service.PostingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class PostingServiceImpl extends ServiceImpl<PostingMapper, Posting> implements PostingService {

    @Autowired
    private PostingMapper postingMapper;

    @Autowired
    private JournalEntryMapper journalEntryMapper;

    @Override
    @Transactional
    public boolean postJournalEntry(Long entryId, String userId) {
        // 检查是否已过账
        Posting existingPosting = postingMapper.selectByEntryId(entryId);
        if (existingPosting != null) {
            return false;
        }

        // 创建过账记录
        Posting posting = new Posting();
        posting.setEntryId(entryId);
        posting.setPostingDate(new Date());
        posting.setPostingUserId(userId);

        // 保存过账记录
        postingMapper.insert(posting);

        // 更新凭证状态为已过账
        journalEntryMapper.updateStatus(entryId, 1);

        return true;
    }

    @Override
    @Transactional
    public boolean cancelPosting(Long entryId) {
        Posting posting = postingMapper.selectById(entryId);
        if (posting == null) {
            return false;
        }

        // 删除过账记录
        postingMapper.deleteById(posting.getId());

        // 更新凭证状态为未过账
        journalEntryMapper.updateStatus(posting.getEntryId(), 0);

        return true;
    }

    @Override
    public Posting getByEntryId(Long entryId) {
        return postingMapper.selectByEntryId(entryId);
    }
}
