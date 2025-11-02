package com.web3.indexer.listener;

import org.springframework.stereotype.Service;

/**
 * 区块链数据抓取服务启动器
 * 使用 @EventListener 监听应用启动事件，在Spring容器完全初始化后启动服务
 */
@Service
public class BlockchainServiceStarter {

    private final BlockchainWebSocketClient webSocketClient; // WebSocket客户端，负责与区块链节点通信
    private final BlockHeightTracker blockHeightTracker; // 区块高度跟踪器，管理断点续传
    private final BlockDataProcessor blockDataProcessor; // 区块数据处理器，解析和存储区块数据
    private final KafkaMessageProducer kafkaMessageProducer;
}
