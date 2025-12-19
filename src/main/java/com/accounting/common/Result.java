package com.accounting.common;

import lombok.Data;

/**
 * 统一响应结果封装类
 * 
 * @param <T> 响应数据的类型
 * @author Accounting Platform
 * @version 1.0.0
 */
@Data
public class Result<T> {
    /**
     * 响应状态码
     */
    private int code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;

    /**
     * 无参构造函数
     */
    public Result() {
    }

    /**
     * 全参构造函数
     * 
     * @param code 状态码
     * @param message 消息
     * @param data 数据
     */
    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功响应（带数据）
     * 
     * @param <T> 数据类型
     * @param data 响应数据
     * @return 成功结果
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }

    /**
     * 成功响应（无数据）
     * 
     * @param <T> 数据类型
     * @return 成功结果
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "success", null);
    }

    /**
     * 错误响应（默认错误码500）
     * 
     * @param <T> 数据类型
     * @return 错误结果
     */
    public static <T> Result<T> error() {
        return new Result<>(500, "", null);
    }

    /**
     * 错误响应（自定义错误消息）
     * 
     * @param <T> 数据类型
     * @param message 错误消息
     * @return 错误结果
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }

    /**
     * 错误响应（自定义错误码和消息）
     * 
     * @param <T> 数据类型
     * @param code 错误码
     * @param message 错误消息
     * @return 错误结果
     */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }
}
