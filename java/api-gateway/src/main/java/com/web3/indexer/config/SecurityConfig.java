package com.web3.indexer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                // 授权配置 - 允许所有请求
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/**").permitAll() // 允许所有路径
                        .anyExchange().permitAll() // 允许所有交换
                )
                // 禁用 CSRF
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                // 禁用 HTTP Basic 认证
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                // 禁用表单登录
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                // 禁用注销
                .logout(ServerHttpSecurity.LogoutSpec::disable)
                .build();
    }
}