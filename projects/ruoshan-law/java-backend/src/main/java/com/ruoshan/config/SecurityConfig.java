package com.ruoshan.law.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/admin/**").authenticated()
                // Content management endpoints
                .requestMatchers("/api/admin/news/**").hasRole("ADMIN")
                .requestMatchers("/api/admin/cases/**").hasRole("ADMIN")
                .requestMatchers("/api/admin/pages/**").hasRole("ADMIN")
                // Business management endpoints
                .requestMatchers("/api/admin/lawyers/**").hasRole("ADMIN")
                .requestMatchers("/api/admin/services/**").hasRole("ADMIN")
                // Customer interaction endpoints
                .requestMatchers("/api/admin/contacts/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers("/api/admin/appointments/**").hasAnyRole("ADMIN", "STAFF")
                // System management endpoints
                .requestMatchers("/api/admin/users/**").hasRole("ADMIN")
                .requestMatchers("/api/admin/settings/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .exceptionHandling()
            .and()
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}

// Additional security configuration classes would be defined in separate files
// This is a placeholder for the security configuration implementation
