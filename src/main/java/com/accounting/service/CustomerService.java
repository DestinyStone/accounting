package com.accounting.service;

import com.accounting.entity.Customer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;

/**
 * 客户服务接口
 */
public interface CustomerService extends IService<Customer> {
    
    /**
     * 分页查询客户列表
     */
    IPage<Customer> page(Integer pageNum, Integer pageSize, Long companyId, String customerCode, String customerName, Integer status);
    
    /**
     * 根据ID查询客户
     */
    Customer getById(Long id);
    
    /**
     * 新增客户
     */
    boolean insert(Customer customer);
    
    /**
     * 更新客户
     */
    boolean update(Customer customer);
    
    /**
     * 删除客户
     */
    boolean delete(Long id);
    
    /**
     * 批量删除客户
     */
    boolean deleteBatch(List<Long> ids);
    
    /**
     * 根据客户编码查询
     */
    Customer getByCode(String customerCode, Long companyId);
    
    /**
     * 根据公司ID查询启用的客户列表
     */
    List<Customer> listActiveByCompanyId(Long companyId);
    
    /**
     * 检查客户名称是否重复
     */
    boolean checkNameDuplicate(String customerName, Long companyId, Long id);
    
    /**
     * 生成客户编码
     */
    String generateCode(Long companyId);
}
