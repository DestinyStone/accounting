package com.accounting.service.impl;

import com.accounting.entity.Customer;
import com.accounting.mapper.CustomerMapper;
import com.accounting.service.CustomerService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 客户服务实现类
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public Page<Customer> page(Integer pageNum, Integer pageSize, Long companyId, String customerCode, String customerName, Integer status) {
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();
        if (customerCode != null && !customerCode.isEmpty()) {
            wrapper.like("customer_code", customerCode);
        }
        if (customerName != null && !customerName.isEmpty()) {
            wrapper.like("customer_name", customerName);
        }
        return baseMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public Customer getById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(Customer customer) {
        // 检查客户编码是否存在
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Customer::getCustomerCode, customer.getCustomerCode());
        Customer existing = customerMapper.selectOne(wrapper);
        if (existing != null) {
            throw new RuntimeException("客户编码已存在");
        }
        return baseMapper.insert(customer) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Customer customer) {
        return baseMapper.updateById(customer) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(List<Long> ids) {
        // 检查是否有相关业务数据
        // TODO: 实现相关业务数据检查
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public Customer getByCode(String customerCode, Long companyId) {
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Customer::getCustomerCode, customerCode);
        return customerMapper.selectOne(wrapper);
    }

    @Override
    public List<Customer> listActiveByCompanyId(Long companyId) {
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Customer::getStatus, 1);
        return customerMapper.selectList(wrapper);
    }

    @Override
    public boolean checkNameDuplicate(String customerName, Long companyId, Long id) {
        return customerMapper.checkNameDuplicate(customerName, companyId, id) > 0;
    }

    @Override
    public String generateCode(Long companyId) {
        // 查询最大客户编码
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("customer_code");
        List<Customer> list = baseMapper.selectList(wrapper);
        if (list != null && !list.isEmpty()) {
            Customer customer = list.get(0);
            String code = customer.getCustomerCode();
            try {
                int num = Integer.parseInt(code.substring(2));
                return String.format("KH%06d", num + 1);
            } catch (Exception e) {
                // 如果编码格式不正确，从1开始
            }
        }
        return "KH000001";
    }
}
