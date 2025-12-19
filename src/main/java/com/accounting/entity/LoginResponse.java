package com.accounting.entity;

import lombok.Data;

@Data
public class LoginResponse {
    private Admin user;
    private String token;
    private String message;
}