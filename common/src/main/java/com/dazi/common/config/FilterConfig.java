package com.dazi.common.config;

import com.dazi.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 过滤器配置
 */
@Configuration
@RequiredArgsConstructor
public class FilterConfig {
    
    private final JwtUtil jwtUtil;
    
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter() {
        FilterRegistrationBean<JwtAuthenticationFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new JwtAuthenticationFilter(jwtUtil));
        registration.addUrlPatterns("/api/*");
        registration.setOrder(1);
        return registration;
    }
}
