package com.accounting.mapper;

import com.accounting.entity.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 员工Mapper接口
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
    
    /**
     * 根据员工编码查询
     */
    Employee selectByCode(@Param("employeeCode") String employeeCode, @Param("companyId") Long companyId);
    
    /**
     * 根据公司ID查询启用的员工
     */
    List<Employee> selectActiveByCompanyId(@Param("companyId") Long companyId);
    
    /**
     * 检查员工名称是否重复
     */
    int checkNameDuplicate(@Param("employeeName") String employeeName, @Param("companyId") Long companyId, @Param("id") Long id);
    
    /**
     * 检查身份证号是否重复
     */
    int checkIdCardDuplicate(@Param("idCard") String idCard, @Param("companyId") Long companyId, @Param("id") Long id);
    
    /**
     * 检查手机号是否重复
     */
    int checkPhoneDuplicate(@Param("phone") String phone, @Param("companyId") Long companyId, @Param("id") Long id);
    
    /**
     * 根据部门查询员工
     */
    List<Employee> selectByDepartment(@Param("department") String department, @Param("companyId") Long companyId);
    
    /**
     * 批量启用员工
     */
    int enableBatch(@Param("ids") List<Long> ids);
    
    /**
     * 批量禁用员工
     */
    int disableBatch(@Param("ids") List<Long> ids);
}
