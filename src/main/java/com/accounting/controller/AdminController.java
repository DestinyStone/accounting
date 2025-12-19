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

@RestController
@RequestMapping(Api.path + "/login")
public class AdminController {

    @Autowired
    private AdminService adminService;

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
