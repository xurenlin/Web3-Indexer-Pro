package com.web3.indexer.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 区块索引器状态响应DTO
 * 用于返回区块数据抓取服务的实时状态信息
 * 
 * @author chain-indexer
 * @version 1.0
 * @since 2024
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class IndexerStatusResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 区块链网络标识 - 唯一标识不同的区块链网络
     * 格式: {CHAIN}_{NETWORK}，例如: ETH_MAINNET, BSC_TESTNET, POLYGON_MAINNET
     */
    @JsonProperty("chainId")
    private String chainId;

    /**
     * 区块链网络ID - 标准的Chain ID数字标识
     * 1: Ethereum Mainnet
     * 5: Goerli Testnet
     * 56: BSC Mainnet
     * 97: BSC Testnet
     * 137: Polygon Mainnet
     * 80001: Polygon Mumbai
     */
    @JsonProperty("networkId")
    private Integer networkId;

    /**
     * 货币符号 - 该链的原生代币符号
     */
    @JsonProperty("currencySymbol")
    private String currencySymbol;

    /**
     * 节点RPC地址 - 当前连接使用的区块链节点端点
     */
    @JsonProperty("nodeEndpoint")
    private String nodeEndpoint;

    /**
     * 最新已处理区块高度 - 索引器最新处理完成的区块号
     */
    @JsonProperty("latestBlock")
    private Long latestBlock;

    /**
     * 同步状态 - 标识索引器是否正在同步区块数据
     */
    @JsonProperty("isSyncing")
    private Boolean isSyncing;

    /**
     * 同步速度 - 每秒处理的区块数量（单位：区块/秒）
     */
    @JsonProperty("blocksPerSecond")
    private Double blocksPerSecond;

    /**
     * 当前链上最新区块 - 区块链网络上的最新区块高度
     */
    @JsonProperty("currentChainBlock")
    private Long currentChainBlock;

    /**
     * 数据库最新区块 - 数据库中存储的最新区块高度
     */
    @JsonProperty("databaseLatestBlock")
    private Long databaseLatestBlock;

    /**
     * 落后区块数 - 当前处理高度与链上最新高度的差距
     * 计算公式：currentChainBlock - latestBlock
     */
    @JsonProperty("blocksBehind")
    private Long blocksBehind;

    /**
     * 同步进度 - 同步完成百分比（0.0 - 1.0）
     * 计算公式：(latestBlock - startBlock) / (currentChainBlock - startBlock)
     */
    @JsonProperty("syncProgress")
    private Double syncProgress;

    /**
     * 总处理区块数 - 自服务启动以来累计处理的区块总数
     */
    @JsonProperty("totalProcessedBlocks")
    private Long totalProcessedBlocks;

    /**
     * 服务运行时间 - 服务持续运行的时间（单位：秒）
     */
    @JsonProperty("uptimeSeconds")
    private Long uptimeSeconds;

    /**
     * 最后更新时间 - 状态信息的最后更新时间
     */
    @JsonProperty("lastUpdated")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdated;

    /**
     * 服务状态 - 服务的整体健康状态
     * RUNNING: 正常运行
     * SYNCING: 正在同步
     * CATCHING_UP: 追赶模式
     * ERROR: 错误状态
     */
    @JsonProperty("serviceStatus")
    private String serviceStatus;

    /**
     * 同步模式 - 当前的同步模式
     * REAL_TIME: 实时监听模式
     * CATCH_UP: 追赶模式
     * HISTORICAL: 历史数据同步
     */
    @JsonProperty("syncMode")
    private String syncMode;

    /**
     * 开始同步区块 - 当前同步任务的起始区块高度
     */
    @JsonProperty("startBlock")
    private Long startBlock;

    /**
     * 目标同步区块 - 当前同步任务的目标区块高度
     */
    @JsonProperty("targetBlock")
    private Long targetBlock;

    /**
     * 预计剩余时间 - 完成同步预计还需要的时间（单位：秒）
     */
    @JsonProperty("estimatedTimeRemaining")
    private Long estimatedTimeRemaining;

    /**
     * 错误信息 - 如果服务处于错误状态，包含错误描述
     */
    @JsonProperty("errorMessage")
    private String errorMessage;

    /**
     * Kafka状态 - Kafka消息队列的生产状态
     */
    @JsonProperty("kafkaStatus")
    private String kafkaStatus;

    /**
     * 已推送消息数 - 推送到Kafka的总消息数量
     */
    @JsonProperty("totalKafkaMessages")
    private Long totalKafkaMessages;

    // 默认构造函数
    public IndexerStatusResponse() {
        this.lastUpdated = LocalDateTime.now();
    }

    // 带参构造函数 - 基础状态
    public IndexerStatusResponse(Long latestBlock, Boolean isSyncing, Double blocksPerSecond,
            Long currentChainBlock, Long databaseLatestBlock) {
        this();
        this.latestBlock = latestBlock;
        this.isSyncing = isSyncing;
        this.blocksPerSecond = blocksPerSecond;
        this.currentChainBlock = currentChainBlock;
        this.databaseLatestBlock = databaseLatestBlock;
        this.calculateDerivedFields();
    }

    /**
     * 计算派生字段
     * 包括：落后区块数、同步进度等
     */
    public void calculateDerivedFields() {
        // 计算落后区块数
        if (this.currentChainBlock != null && this.latestBlock != null) {
            this.blocksBehind = this.currentChainBlock - this.latestBlock;
        }

        // 计算同步进度
        if (this.startBlock != null && this.targetBlock != null && this.latestBlock != null) {
            long totalBlocks = this.targetBlock - this.startBlock;
            long processedBlocks = this.latestBlock - this.startBlock;
            if (totalBlocks > 0) {
                this.syncProgress = (double) processedBlocks / totalBlocks;
            }
        }

        // 计算预计剩余时间
        if (this.blocksPerSecond != null && this.blocksPerSecond > 0 && this.blocksBehind != null) {
            this.estimatedTimeRemaining = (long) (this.blocksBehind / this.blocksPerSecond);
        }
    }

}
