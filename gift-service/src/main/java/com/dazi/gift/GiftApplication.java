package com.dazi.gift;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GiftApplication {
    public static void main(String[] args) {
        SpringApplication.run(GiftApplication.class, args);
    }
}
