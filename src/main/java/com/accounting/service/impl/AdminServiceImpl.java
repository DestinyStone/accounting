package com.accounting.service.impl;

import com.accounting.entity.Admin;
import com.accounting.entity.LoginRequest;
import com.accounting.entity.LoginResponse;
import com.accounting.mapper.AdminMapper;
import com.accounting.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

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

    @Override
    public Admin getAdminByUsername(String username) {
        return adminMapper.selectByUsername(username);
    }

    @Override
    public boolean updateAdmin(Admin admin) {
        return adminMapper.updateById(admin) > 0;
    }
}
