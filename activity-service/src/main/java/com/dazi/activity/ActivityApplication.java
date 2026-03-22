package com.dazi.activity;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.dazi.activity.repository")
public class ActivityApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ActivityApplication.class, args);
    }
}
