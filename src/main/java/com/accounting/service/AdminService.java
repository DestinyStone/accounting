package com.accounting.service;

import com.accounting.entity.Admin;
import com.accounting.entity.LoginRequest;
import com.accounting.entity.LoginResponse;

/**
 * 管理员服务接口
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
public interface AdminService {
    /**
     * 管理员登录
     * 
     * @param loginRequest 登录请求，包含用户名和密码
     * @return 登录响应，包含用户信息和token
     */
    LoginResponse login(LoginRequest loginRequest);
    
    /**
     * 根据用户名查询管理员
     * 
     * @param username 用户名
     * @return 管理员对象
     */
    Admin getAdminByUsername(String username);
    
    /**
     * 更新管理员信息
     * 
     * @param admin 管理员对象
     * @return 是否更新成功
     */
    boolean updateAdmin(Admin admin);
}