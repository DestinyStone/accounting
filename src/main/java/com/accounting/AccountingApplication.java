package com.accounting;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 会计平台主启动类
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
@SpringBootApplication
@MapperScan("com.accounting.mapper")
public class AccountingApplication {

    /**
     * 应用程序入口方法
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(AccountingApplication.class, args);
    }

}