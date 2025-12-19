package com.accounting.mapper;

import com.accounting.entity.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 客户Mapper接口
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {

    /**
     * 检查客户名称是否重复
     */
    int checkNameDuplicate(String customerName, Long companyId, Long id);
}
