package com.web3.indexer.common.config;

import com.web3.indexer.common.exception.GlobalExceptionHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExceptionAutoConfiguration {
    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}
