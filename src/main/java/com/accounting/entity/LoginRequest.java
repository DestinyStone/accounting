package com.accounting.entity;

import lombok.Data;

/**
 * 登录请求实体类
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
@Data
public class LoginRequest {
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
}