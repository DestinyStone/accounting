package com.accounting.controller;

import com.accounting.common.Api;
import com.accounting.common.Result;
import com.accounting.entity.LoginRequest;
import com.accounting.entity.LoginResponse;
import com.accounting.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员控制器
 * 处理管理员登录相关请求
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
@RestController
@RequestMapping(Api.path + "/login")
public class AdminController {

    /**
     * 管理员服务
     */
    @Autowired
    private AdminService adminService;

    /**
     * 管理员登录
     * 
     * @param loginRequest 登录请求，包含用户名和密码
     * @return 登录响应结果，包含用户信息和token
     */
    @PostMapping
    public Result<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = adminService.login(loginRequest);
        if (response.getToken() != null) {
            return Result.success(response);
        } else {
            return Result.error(response.getMessage());
        }
    }
}
