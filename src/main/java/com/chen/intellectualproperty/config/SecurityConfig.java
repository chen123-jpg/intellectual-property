package com.chen.intellectualproperty.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 如果已有独立的 PasswordEncoderConfig，这里不再定义 passwordEncoder()，避免冲突

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()   // 所有请求直接放行
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/api/user/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler((req, res, auth) -> {
                            res.setContentType("application/json;charset=utf-8");
                            res.getWriter().write("{\"message\":\"登录成功\"}");
                        })
                        .failureHandler((req, res, ex) -> {
                            res.setContentType("application/json;charset=utf-8");
                            res.setStatus(401);
                            res.getWriter().write("{\"error\":\"登录失败：" + ex.getMessage() + "\"}");
                        })
                )
                .logout(logout -> logout
                        .logoutUrl("/api/user/logout")
                        .logoutSuccessHandler((req, res, auth) -> {
                            res.setContentType("application/json;charset=utf-8");
                            res.getWriter().write("{\"message\":\"已登出\"}");
                        })
                )
                .csrf(csrf -> csrf.disable());
        return http.build();
    }
}