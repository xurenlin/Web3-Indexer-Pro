-- 创建区块索引器状态表
CREATE TABLE indexer_status (
    id BIGSERIAL PRIMARY KEY,
    
    -- 基础网络信息
    chain_id VARCHAR(50) NOT NULL,
    network_id INTEGER NOT NULL,
    currency_symbol VARCHAR(20),
    node_endpoint TEXT,
    
    -- 区块高度信息
    latest_block BIGINT,
    current_chain_block BIGINT,
    database_latest_block BIGINT,
    blocks_behind BIGINT,
    
    -- 同步状态信息
    is_syncing BOOLEAN DEFAULT FALSE,
    blocks_per_second DECIMAL(10,4),
    sync_progress DECIMAL(5,4),
    
    -- 统计信息
    total_processed_blocks BIGINT DEFAULT 0,
    uptime_seconds BIGINT DEFAULT 0,
    total_kafka_messages BIGINT DEFAULT 0,
    
    -- 同步配置信息
    start_block BIGINT,
    target_block BIGINT,
    estimated_time_remaining BIGINT,
    
    -- 状态枚举字段
    service_status VARCHAR(20),
    sync_mode VARCHAR(20),
    kafka_status VARCHAR(50),
    
    -- 错误信息
    error_message TEXT,
    
    -- 时间戳
    last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- 唯一约束
    UNIQUE (chain_id, network_id)
);

-- 添加表约束（建表后添加）
ALTER TABLE indexer_status 
ADD CONSTRAINT chk_service_status 
CHECK (service_status IN ('RUNNING', 'SYNCING', 'CATCHING_UP', 'ERROR'));

ALTER TABLE indexer_status 
ADD CONSTRAINT chk_sync_mode 
CHECK (sync_mode IN ('REAL_TIME', 'CATCH_UP', 'HISTORICAL'));

-- 添加表注释
COMMENT ON TABLE indexer_status IS '区块索引器状态表 - 存储各区块链网络索引器的实时运行状态';

-- 添加字段注释
COMMENT ON COLUMN indexer_status.id IS '主键ID，自增长';
COMMENT ON COLUMN indexer_status.chain_id IS '区块链网络标识 - 唯一标识不同的区块链网络，格式: {CHAIN}_{NETWORK}，例如: ETH_MAINNET, BSC_TESTNET, POLYGON_MAINNET';
COMMENT ON COLUMN indexer_status.network_id IS '区块链网络ID - 标准的Chain ID数字标识，1: Ethereum Mainnet, 5: Goerli Testnet, 56: BSC Mainnet, 97: BSC Testnet, 137: Polygon Mainnet, 80001: Polygon Mumbai';
COMMENT ON COLUMN indexer_status.currency_symbol IS '货币符号 - 该链的原生代币符号';
COMMENT ON COLUMN indexer_status.node_endpoint IS '节点RPC地址 - 当前连接使用的区块链节点端点';
COMMENT ON COLUMN indexer_status.latest_block IS '最新已处理区块高度 - 索引器最新处理完成的区块号';
COMMENT ON COLUMN indexer_status.current_chain_block IS '当前链上最新区块 - 区块链网络上的最新区块高度';
COMMENT ON COLUMN indexer_status.database_latest_block IS '数据库最新区块 - 数据库中存储的最新区块高度';
COMMENT ON COLUMN indexer_status.blocks_behind IS '落后区块数 - 当前处理高度与链上最新高度的差距，计算公式：current_chain_block - latest_block';
COMMENT ON COLUMN indexer_status.is_syncing IS '同步状态 - 标识索引器是否正在同步区块数据';
COMMENT ON COLUMN indexer_status.blocks_per_second IS '同步速度 - 每秒处理的区块数量（单位：区块/秒）';
COMMENT ON COLUMN indexer_status.sync_progress IS '同步进度 - 同步完成百分比（0.0 - 1.0），计算公式：(latest_block - start_block) / (target_block - start_block)';
COMMENT ON COLUMN indexer_status.total_processed_blocks IS '总处理区块数 - 自服务启动以来累计处理的区块总数';
COMMENT ON COLUMN indexer_status.uptime_seconds IS '服务运行时间 - 服务持续运行的时间（单位：秒）';
COMMENT ON COLUMN indexer_status.total_kafka_messages IS '已推送消息数 - 推送到Kafka的总消息数量';
COMMENT ON COLUMN indexer_status.start_block IS '开始同步区块 - 当前同步任务的起始区块高度';
COMMENT ON COLUMN indexer_status.target_block IS '目标同步区块 - 当前同步任务的目标区块高度';
COMMENT ON COLUMN indexer_status.estimated_time_remaining IS '预计剩余时间 - 完成同步预计还需要的时间（单位：秒）';
COMMENT ON COLUMN indexer_status.service_status IS '服务状态 - 服务的整体健康状态：RUNNING: 正常运行, SYNCING: 正在同步, CATCHING_UP: 追赶模式, ERROR: 错误状态';
COMMENT ON COLUMN indexer_status.sync_mode IS '同步模式 - 当前的同步模式：REAL_TIME: 实时监听模式, CATCH_UP: 追赶模式, HISTORICAL: 历史数据同步';
COMMENT ON COLUMN indexer_status.kafka_status IS 'Kafka状态 - Kafka消息队列的生产状态';
COMMENT ON COLUMN indexer_status.error_message IS '错误信息 - 如果服务处于错误状态，包含错误描述';
COMMENT ON COLUMN indexer_status.last_updated IS '最后更新时间 - 状态信息的最后更新时间';
COMMENT ON COLUMN indexer_status.created_at IS '创建时间 - 记录创建时间';

-- 创建索引（建表后添加）
CREATE INDEX idx_indexer_status_chain ON indexer_status(chain_id);
CREATE INDEX idx_indexer_status_network ON indexer_status(network_id);
CREATE INDEX idx_indexer_status_syncing ON indexer_status(is_syncing);
CREATE INDEX idx_indexer_status_updated ON indexer_status(last_updated);
CREATE INDEX idx_indexer_status_latest_block ON indexer_status(latest_block);

-- 插入示例数据
INSERT INTO indexer_status (
    chain_id, network_id, currency_symbol, node_endpoint,
    latest_block, current_chain_block, database_latest_block, blocks_behind,
    is_syncing, blocks_per_second, sync_progress,
    service_status, sync_mode, kafka_status,
    start_block, target_block, estimated_time_remaining,
    total_processed_blocks, uptime_seconds, total_kafka_messages,
    error_message
) VALUES (
    'ETH_MAINNET', 1, 'ETH', 'https://mainnet.infura.io/v3/your-project-id',
    18500000, 18500100, 18500000, 100,
    true, 12.5, 0.995,
    'SYNCING', 'REAL_TIME', 'ACTIVE',
    18000000, 18500000, 8,
    500000, 86400, 2500000,
    NULL
);