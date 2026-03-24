package com.dazi.rtc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.dazi.rtc.repository")
public class RtcApplication {
    public static void main(String[] args) {
        SpringApplication.run(RtcApplication.class, args);
    }
}
