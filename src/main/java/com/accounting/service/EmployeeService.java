package com.accounting.service;

import com.accounting.entity.Employee;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;

/**
 * 员工服务接口
 */
public interface EmployeeService {
    
    /**
     * 分页查询员工列表
     */
    IPage<Employee> page(Integer pageNum, Integer pageSize, Long companyId, String employeeCode,
                             String employeeName, String department, Integer status);
    
    /**
     * 根据ID查询员工
     */
    Employee getById(Long id);
    
    /**
     * 根据员工编码查询
     */
    Employee getByCode(String employeeCode, Long companyId);
    
    /**
     * 根据公司ID查询启用的员工
     */
    List<Employee> getActiveByCompanyId(Long companyId);
    
    /**
     * 新增员工
     */
    boolean insert(Employee employee);
    
    /**
     * 修改员工
     */
    boolean update(Employee employee);
    
    /**
     * 删除员工
     */
    boolean delete(Long id);
    
    /**
     * 批量删除员工
     */
    boolean deleteBatch(List<Long> ids);
    
    /**
     * 批量启用员工
     */
    boolean enableBatch(List<Long> ids);
    
    /**
     * 批量禁用员工
     */
    boolean disableBatch(List<Long> ids);
    
    /**
     * 检查员工名称是否存在
     */
    boolean checkNameExists(String employeeName, Long companyId, Long id);
    
    /**
     * 检查身份证号是否存在
     */
    boolean checkIdCardExists(String idCard, Long companyId, Long id);
    
    /**
     * 检查手机号是否存在
     */
    boolean checkPhoneExists(String phone, Long companyId, Long id);
    
    /**
     * 生成员工编码
     */
    String generateEmployeeCode(Long companyId);
    
    /**
     * 根据部门查询员工
     */
    List<Employee> getByDepartment(String department, Long companyId);
    
    /**
     * 验证身份证号格式
     */
    boolean validateIdCard(String idCard);
    
    /**
     * 验证手机号格式
     */
    boolean validatePhone(String phone);
}
