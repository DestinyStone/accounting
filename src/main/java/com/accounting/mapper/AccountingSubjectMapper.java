package com.accounting.mapper;

import com.accounting.entity.AccountingSubject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;

/**
 * 会计科目Mapper接口
 */
public interface AccountingSubjectMapper extends BaseMapper<AccountingSubject> {

    /**
     * 根据父级ID查询科目列表
     * @param parentId 父级科目ID
     * @return 科目列表
     */
    List<AccountingSubject> selectByParentId(Long parentId);
    
    /**
     * 根据类型查询科目列表
     * @param type 科目类型
     * @return 科目列表
     */
    List<AccountingSubject> selectByType(String type);
    
    /**
     * 查询所有启用的末级科目
     * @return 末级科目列表
     */
    List<AccountingSubject> selectEnabledLeafSubjects();
}