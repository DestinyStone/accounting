package com.accounting.service;

import com.accounting.entity.Supplier;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;

/**
 * 供应商服务接口
 */
public interface SupplierService extends IService<Supplier> {

    /**
     * 分页查询供应商列表
     * @param page 分页参数
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<Supplier> selectPage(Page<Supplier> page, Map<String, Object> params);

    /**
     * 根据ID查询供应商信息
     * @param id 供应商ID
     * @return 供应商信息
     */
    Supplier selectById(Long id);

    /**
     * 新增供应商
     * @param supplier 供应商信息
     * @return 是否成功
     */
    boolean insert(Supplier supplier);

    /**
     * 更新供应商信息
     * @param supplier 供应商信息
     * @return 是否成功
     */
    boolean update(Supplier supplier);

    /**
     * 删除供应商
     * @param id 供应商ID
     * @return 是否成功
     */
    boolean delete(Long id);

    /**
     * 批量删除供应商
     * @param ids 供应商ID列表
     * @return 是否成功
     */
    boolean deleteBatch(List<Long> ids);

    /**
     * 根据供应商编码查询供应商
     * @param supplierCode 供应商编码
     * @return 供应商信息
     */
    Supplier selectByCode(String supplierCode);

    /**
     * 检查供应商名称是否已存在
     * @param supplierName 供应商名称
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean checkNameExists(String supplierName, Long excludeId);
}
