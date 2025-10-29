package com.web3.indexer.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 区块链配置类
 */
@Data
@Component
@ConfigurationProperties(prefix = "blockchain")
public class BlockchainConfig {

    private Map<String, ChainConfig> chains;

    @Data
    public static class ChainConfig {
        private String chainId;
        private String chainName;
        private Integer networkId;
        private String currencySymbol;
        private String rpcEndpoint;
        private Boolean enabled;
        private Long startBlock;
    }
}
