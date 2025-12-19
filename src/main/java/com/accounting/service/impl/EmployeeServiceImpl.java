package com.accounting.service.impl;

import com.accounting.entity.Employee;
import com.accounting.mapper.EmployeeMapper;
import com.accounting.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 员工服务实现类
 */
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public IPage<Employee> page(Integer pageNum, Integer pageSize, Long companyId, String employeeCode,
                                 String employeeName, String department, Integer status) {
        Page<Employee> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        if (status != null) {
            wrapper.eq("status", status);
        }
        return employeeMapper.selectPage(page, wrapper);
    }

    @Override
    public Employee getById(Long id) {
        return employeeMapper.selectById(id);
    }

    @Override
    public Employee getByCode(String employeeCode, Long companyId) {
        return employeeMapper.selectByCode(employeeCode, companyId);
    }

    @Override
    public List<Employee> getActiveByCompanyId(Long companyId) {
        return employeeMapper.selectActiveByCompanyId(companyId);
    }

    @Override
    public boolean insert(Employee employee) {
        // 生成员工编码
        employee.setEmployeeCode(generateEmployeeCode(1L));
        return employeeMapper.insert(employee) > 0;
    }

    @Override
    public boolean update(Employee employee) {
        return employeeMapper.updateById(employee) > 0;
    }

    @Override
    public boolean delete(Long id) {
        return employeeMapper.deleteById(id) > 0;
    }

    @Override
    public boolean deleteBatch(List<Long> ids) {
        return employeeMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public boolean enableBatch(List<Long> ids) {
        return employeeMapper.enableBatch(ids) > 0;
    }

    @Override
    public boolean disableBatch(List<Long> ids) {
        return employeeMapper.disableBatch(ids) > 0;
    }

    @Override
    public boolean checkNameExists(String employeeName, Long companyId, Long id) {
        return employeeMapper.checkNameDuplicate(employeeName, companyId, id) > 0;
    }

    @Override
    public boolean checkIdCardExists(String idCard, Long companyId, Long id) {
        return employeeMapper.checkIdCardDuplicate(idCard, companyId, id) > 0;
    }

    @Override
    public boolean checkPhoneExists(String phone, Long companyId, Long id) {
        return employeeMapper.checkPhoneDuplicate(phone, companyId, id) > 0;
    }

    @Override
    public String generateEmployeeCode(Long companyId) {
        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        List<Employee> list = employeeMapper.selectList(wrapper);

        if (list.isEmpty()) {
            return "EMP" + companyId + "001";
        } else {
            String lastCode = list.get(0).getEmployeeCode();
            String numStr = lastCode.substring(lastCode.length() - 3);
            int num = Integer.parseInt(numStr) + 1;
            return lastCode.substring(0, lastCode.length() - 3) + String.format("%03d", num);
        }
    }

    @Override
    public List<Employee> getByDepartment(String department, Long companyId) {
        return employeeMapper.selectByDepartment(department, companyId);
    }

    @Override
    public boolean validateIdCard(String idCard) {
        // 简单的身份证号格式验证
        String regex = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";
        return Pattern.matches(regex, idCard);
    }

    @Override
    public boolean validatePhone(String phone) {
        // 简单的手机号格式验证
        String regex = "^1[3-9]\\d{9}$";
        return Pattern.matches(regex, phone);
    }
}
