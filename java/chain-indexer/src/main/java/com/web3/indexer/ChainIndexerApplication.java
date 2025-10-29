package com.web3.indexer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@MapperScan("com.web3.indexer.mapper")
public class ChainIndexerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChainIndexerApplication.class, args);
    }
}