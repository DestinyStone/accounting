package com.accounting.service.impl;

import com.accounting.entity.Supplier;
import com.accounting.mapper.SupplierMapper;
import com.accounting.service.SupplierService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

/**
 * 供应商服务实现类
 */
@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements SupplierService {

    @Override
    public IPage<Supplier> selectPage(Page<Supplier> page, Map<String, Object> params) {
        QueryWrapper<Supplier> wrapper = new QueryWrapper<>();

        // 获取查询参数
        String supplierName = params.get("supplierName") != null ? params.get("supplierName").toString() : null;
        String supplierCode = params.get("supplierCode") != null ? params.get("supplierCode").toString() : null;

        // 设置查询条件
        if (supplierName != null && !supplierName.isEmpty()) {
            wrapper.like("supplier_name", supplierName);
        }
        if (supplierCode != null && !supplierCode.isEmpty()) {
            wrapper.like("supplier_code", supplierCode);
        }

        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public Supplier selectById(Long id) {
        return baseMapper.selectById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insert(Supplier supplier) {
        // 生成供应商编码（如果未提供）
        if (supplier.getSupplierCode() == null || supplier.getSupplierCode().isEmpty()) {
            supplier.setSupplierCode(generateSupplierCode(1L));
        }

        // 设置默认状态为启用
        if (supplier.getStatus() == null) {
            supplier.setStatus(1);
        }

        return baseMapper.insert(supplier) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(Supplier supplier) {
        return baseMapper.updateById(supplier) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delete(Long id) {
        return baseMapper.deleteById(id) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteBatch(List<Long> ids) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public Supplier selectByCode(String supplierCode) {
        QueryWrapper<Supplier> wrapper = new QueryWrapper<>();
        wrapper.eq("supplier_code", supplierCode);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public boolean checkNameExists(String supplierName, Long excludeId) {
        QueryWrapper<Supplier> wrapper = new QueryWrapper<>();
        wrapper.eq("supplier_name", supplierName);
        if (excludeId != null) {
            wrapper.ne("id", excludeId);
        }
        return baseMapper.selectCount(wrapper) > 0;
    }

    /**
     * 生成供应商编码
     * @param companyId 公司ID
     * @return 供应商编码
     */
    private String generateSupplierCode(Long companyId) {
        // 获取当前最大的供应商编码
        QueryWrapper<Supplier> wrapper = new QueryWrapper<>();
        wrapper
               .orderByDesc("supplier_code")
               .last("LIMIT 1");

        Supplier lastSupplier = baseMapper.selectOne(wrapper);

        // 如果没有供应商，则从001开始
        if (lastSupplier == null || lastSupplier.getSupplierCode() == null) {
            return "SUP001";
        }

        // 提取数字部分并加1
        String lastCode = lastSupplier.getSupplierCode();
        String numStr = lastCode.replaceAll("\\D+", "");
        int num = Integer.parseInt(numStr) + 1;

        // 格式化为3位数字
        return String.format("SUP%03d", num);
    }
}
