package com.accounting.service;

import com.accounting.entity.AccountingSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 会计科目服务接口
 */
public interface AccountingSubjectService extends IService<AccountingSubject> {

    /**
     * 获取科目树形结构
     * @return 科目树列表
     */
    List<AccountingSubject> getSubjectTree();
    
    /**
     * 根据父级ID获取子科目
     * @param parentId 父级科目ID
     * @return 子科目列表
     */
    List<AccountingSubject> getChildrenByParentId(Long parentId);
    
    /**
     * 新增科目
     * @param subject 科目信息
     * @return 是否成功
     */
    boolean addSubject(AccountingSubject subject);
    
    /**
     * 更新科目
     * @param subject 科目信息
     * @return 是否成功
     */
    boolean updateSubject(AccountingSubject subject);
    
    /**
     * 删除科目
     * @param id 科目ID
     * @return 是否成功
     */
    boolean deleteSubject(Long id);
    
    /**
     * 启用/禁用科目
     * @param id 科目ID
     * @param enabled 是否启用
     * @return 是否成功
     */
    boolean enableSubject(Long id, Boolean enabled);
    
    /**
     * 获取所有启用的末级科目
     * @return 末级科目列表
     */
    List<AccountingSubject> getEnabledLeafSubjects();
    
    /**
     * 绑定科目到业务类型
     * @param id 科目ID
     * @param businessType 业务类型（PURCHASE-采购单，SALES-销售单，EXPENSE-员工费用，TAX-税务处理）
     * @return 是否成功
     */
    boolean bindBusinessType(Long id, String businessType);
    
    /**
     * 取消科目绑定
     * @param id 科目ID
     * @return 是否成功
     */
    boolean unbindBusinessType(Long id);
    
    /**
     * 根据业务类型获取绑定的科目
     * @param businessType 业务类型
     * @return 绑定的科目，如果不存在则返回null
     */
    AccountingSubject getByBusinessType(String businessType);
}