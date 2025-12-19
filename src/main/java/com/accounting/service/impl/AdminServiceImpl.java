package com.accounting.service.impl;

import com.accounting.entity.Admin;
import com.accounting.entity.LoginRequest;
import com.accounting.entity.LoginResponse;
import com.accounting.mapper.AdminMapper;
import com.accounting.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 管理员服务实现类
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
@Service
public class AdminServiceImpl implements AdminService {

    /**
     * 管理员Mapper
     */
    @Autowired
    private AdminMapper adminMapper;

    /**
     * 管理员登录
     * 
     * @param loginRequest 登录请求，包含用户名和密码
     * @return 登录响应，包含用户信息和token
     */
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        LoginResponse response = new LoginResponse();
        // 根据用户名查询管理员
        Admin admin = adminMapper.selectByUsername(loginRequest.getUsername());
        if (admin == null) {
            response.setMessage("用户名不存在");
            return response;
        }
        // 简单的密码校验
        if (!admin.getPassword().equals(loginRequest.getPassword())) {
            response.setMessage("密码错误");
            return response;
        }
        // 登录成功
        response.setUser(admin);
        response.setToken("token_" + admin.getId() + "_" + System.currentTimeMillis());
        response.setMessage("登录成功");
        return response;
    }

    /**
     * 根据用户名查询管理员
     * 
     * @param username 用户名
     * @return 管理员对象
     */
    @Override
    public Admin getAdminByUsername(String username) {
        return adminMapper.selectByUsername(username);
    }

    /**
     * 更新管理员信息
     * 
     * @param admin 管理员对象
     * @return 是否更新成功
     */
    @Override
    public boolean updateAdmin(Admin admin) {
        return adminMapper.updateById(admin) > 0;
    }
}
