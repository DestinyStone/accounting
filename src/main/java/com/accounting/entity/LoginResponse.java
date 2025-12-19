package com.accounting.entity;

import lombok.Data;

/**
 * 登录响应实体类
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
@Data
public class LoginResponse {
    /**
     * 用户信息
     */
    private Admin user;
    
    /**
     * 认证令牌
     */
    private String token;
    
    /**
     * 响应消息
     */
    private String message;
}