package com.accounting.service.impl;

import com.accounting.entity.*;
import com.accounting.mapper.*;
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
    
    @Autowired
    private AdminMapper adminMapper;
    
    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;
    
    @Autowired
    private SalesOrderMapper salesOrderMapper;
    
    @Autowired
    private EmployeeMapper employeeMapper;
    
    @Autowired
    private EmployeeExpenseMapper employeeExpenseMapper;

    @Override
    @Transactional
    public boolean postJournalEntry(Long entryId, Long userId) {
        // 检查凭证是否存在
        JournalEntry entry = journalEntryMapper.selectById(entryId);
        if (entry == null) {
            throw new RuntimeException("凭证不存在");
        }
        
        // 检查是否已过账
        Posting existingPosting = postingMapper.selectByEntryId(entryId);
        if (existingPosting != null) {
            throw new RuntimeException("该凭证已过账，不能重复过账");
        }
        
        // 检查凭证状态
        if (entry.getStatus() == 1) {
            throw new RuntimeException("该凭证已过账");
        }

        // 获取过账人信息
        String postingUserName = "系统用户";
        if (userId != null) {
            Admin admin = adminMapper.selectById(userId);
            if (admin != null) {
                postingUserName = admin.getName();
            }
        }

        // 创建过账记录
        Posting posting = new Posting();
        posting.setEntryId(entryId);
        posting.setPostingDate(new Date());
        posting.setPostingUserId(userId);
        posting.setPostingUserName(postingUserName);
        posting.setRemark("凭证过账");
        // 标注业务来源（从凭证冗余到过账记录，便于查询）
        posting.setBusinessType(entry.getBusinessType());
        posting.setBusinessId(entry.getBusinessId());

        // 保存过账记录
        postingMapper.insert(posting);

        // 更新凭证状态为已过账
        journalEntryMapper.updateStatus(entryId, 1); // 1-已过账

        return true;
    }

    @Override
    @Transactional
    public boolean cancelPosting(Long entryId) {
        // 根据凭证ID查找过账记录
        Posting posting = postingMapper.selectByEntryId(entryId);
        if (posting == null) {
            throw new RuntimeException("该凭证未过账，无法取消");
        }

        // 检查凭证是否存在
        com.accounting.entity.JournalEntry entry = journalEntryMapper.selectById(entryId);
        if (entry == null) {
            throw new RuntimeException("凭证不存在");
        }
        
        // 检查凭证状态
        if (entry.getStatus() != 1) {
            throw new RuntimeException("只有已过账的凭证才能取消过账");
        }

        // 删除过账记录
        postingMapper.deleteById(posting.getId());

        // 更新凭证状态为未过账
        journalEntryMapper.updateStatus(entryId, 0);

        return true;
    }

    @Override
    public Posting getByEntryId(Long entryId) {
        return postingMapper.selectByEntryId(entryId);
    }
    
    /**
     * 获取业务来源信息
     * 
     * @param posting 过账记录对象
     * @return 业务来源信息描述
     */
    public String getBusinessSourceInfo(Posting posting) {
        if (posting.getBusinessType() == null || posting.getBusinessId() == null) {
            return "未知来源";
        }
        
        String businessType = posting.getBusinessType();
        Long businessId = posting.getBusinessId();
        
        try {
            switch (businessType) {
                case "PURCHASE":
                    PurchaseOrder purchaseOrder = purchaseOrderMapper.selectById(businessId);
                    if (purchaseOrder != null) {
                        return "采购订单：" + purchaseOrder.getOrderNumber();
                    }
                    break;
                case "SALES":
                    SalesOrder salesOrder = salesOrderMapper.selectById(businessId);
                    if (salesOrder != null) {
                        return "销售订单：" + salesOrder.getOrderNumber();
                    }
                    break;
                case "SALARY":
                    Employee employee = employeeMapper.selectById(businessId);
                    if (employee != null) {
                        return "员工工资：" + employee.getEmployeeName();
                    }
                    break;
                case "EXPENSE":
                    EmployeeExpense expense = employeeExpenseMapper.selectById(businessId);
                    if (expense != null) {
                        return "员工费用：" + expense.getExpenseNo();
                    }
                    return "员工费用：" + businessId;
                default:
                    return businessType + "：" + businessId;
            }
        } catch (Exception e) {
            // 忽略异常，返回默认信息
        }
        
        return businessType + "：" + businessId;
    }
}
