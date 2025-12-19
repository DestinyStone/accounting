package com.accounting.service;

import com.accounting.entity.Admin;
import com.accounting.entity.LoginRequest;
import com.accounting.entity.LoginResponse;

public interface AdminService {
    LoginResponse login(LoginRequest loginRequest);
    Admin getAdminByUsername(String username);
    boolean updateAdmin(Admin admin);
}