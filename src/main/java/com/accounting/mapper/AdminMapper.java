package com.accounting.mapper;

import com.accounting.entity.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 管理员Mapper接口
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
public interface AdminMapper extends BaseMapper<Admin> {
    /**
     * 根据用户名查询管理员
     * 
     * @param username 用户名
     * @return 管理员对象
     */
    Admin selectByUsername(String username);
}