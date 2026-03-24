package com.dazi.vip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * VIP服务启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
public class VipApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(VipApplication.class, args);
    }
}
