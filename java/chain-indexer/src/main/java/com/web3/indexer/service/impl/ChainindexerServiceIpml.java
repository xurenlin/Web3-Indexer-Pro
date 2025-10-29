package com.web3.indexer.service.impl;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.web3.indexer.config.BlockchainConfig;
import com.web3.indexer.config.BlockchainConfig.ChainConfig;
import com.web3.indexer.factory.Web3jFactory;
import com.web3.indexer.mapper.IndexerStatusMapper;
import com.web3.indexer.model.dto.IndexerStatusResponse;
import com.web3.indexer.service.ChainindexerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthSyncing;

@Service
public class ChainindexerServiceIpml implements ChainindexerService {

    @Autowired
    private Web3jFactory web3jFactory;
    @Autowired
    private BlockchainConfig blockchainConfig;
    @Autowired
    private IndexerStatusMapper indexerStatusMapper;

    // 存储各链的状态信息
    private final Map<String, ChainStatus> chainStatusMap = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> processedBlocksMap = new ConcurrentHashMap<>();
    private final long startTime = System.currentTimeMillis();

    @Override
    public IndexerStatusResponse indexerStatus(String chainId) {
        // 从数据库获取索引器进度,看看是否存在当前链的索引，没有就新增，有就修改状态
        IndexerStatusResponse status = indexerStatusMapper.getIndexerStatus(chainId);
        try {
            // 根据chainId获取配置
            ChainConfig chainConfig = blockchainConfig.getChains().get(chainId);
            if (chainConfig == null) {
                status.setServiceStatus("ERROR");
                status.setErrorMessage("Chain not configured: " + chainId);
                return status;
            }
            // 设置链基础信息
            status.setChainId(chainConfig.getChainId());
            status.setNetworkId(chainConfig.getNetworkId());
            status.setCurrencySymbol(chainConfig.getCurrencySymbol());
            status.setNodeEndpoint(chainConfig.getRpcEndpoint());
            // 获取web3j
            Web3j web3j = web3jFactory.getWeb3j(chainId, chainConfig.getRpcEndpoint());
            // ETH获取最新区块
            EthBlockNumber ethBlockNumber = web3j.ethBlockNumber().send();
            status.setCurrentChainBlock(ethBlockNumber.getBlockNumber().longValue());
            // 获取同步状态
            EthSyncing ethSyncing = web3j.ethSyncing().send();
            status.setIsSyncing(ethSyncing.isSyncing());
            // 计算性能指标
            calculatePerformanceMetrics(chainId, status);
            // 设置服务状态
            status.setServiceStatus(ethSyncing.isSyncing() ? "SYNCING" : "RUNNING");
            status.setSyncMode(ethSyncing.isSyncing() ? "CATCH_UP" : "REAL_TIME");
            // 计算派生字段
            status.calculateDerivedFields();
            // 保存到数据库
            indexerStatusMapper.updateIndexerStatus(status);
        } catch (IOException e) {
            status.setServiceStatus("ERROR");
            status.setErrorMessage("Failed to connect to blockchain node: " + e.getMessage());
        }
        return status;
    }

    /**
     * 计算性能指标
     */
    private void calculatePerformanceMetrics(String chainId, IndexerStatusResponse response) {
        ChainStatus chainStatus = chainStatusMap.computeIfAbsent(chainId,
                k -> new ChainStatus());

        AtomicLong processedBlocks = processedBlocksMap.computeIfAbsent(chainId,
                k -> new AtomicLong(0));

        long currentTime = System.currentTimeMillis();
        long timeDiff = currentTime - chainStatus.lastUpdateTime;

        // 计算区块同步速度
        if (timeDiff > 0 && chainStatus.lastBlockNumber != null) {
            long blocksProcessed = response.getLatestBlock() - chainStatus.lastBlockNumber;
            if (blocksProcessed > 0) {
                double blocksPerSecond = (blocksProcessed * 1000.0) / timeDiff;
                response.setBlocksPerSecond(blocksPerSecond);
            }
        }

        // 更新状态
        chainStatus.lastBlockNumber = response.getLatestBlock();
        chainStatus.lastUpdateTime = currentTime;

        // 设置总处理区块数
        response.setTotalProcessedBlocks(processedBlocks.get());

        // 设置运行时间
        response.setUptimeSeconds((currentTime - startTime) / 1000);
    }

    /**
     * 链状态内部类
     */
    private static class ChainStatus {
        private Long lastBlockNumber;
        private long lastUpdateTime = System.currentTimeMillis();
    }

}
