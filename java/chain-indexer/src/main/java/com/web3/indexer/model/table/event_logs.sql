-- 创建表
CREATE TABLE event_logs (
    id BIGSERIAL PRIMARY KEY,
    chain_id VARCHAR(50) NOT NULL,
    transaction_id BIGINT NOT NULL,
    block_header_id BIGINT NOT NULL,
    log_index INTEGER NOT NULL,
    block_number BIGINT NOT NULL,
    transaction_hash VARCHAR(66) NOT NULL,
    address VARCHAR(42) NOT NULL,
    topics TEXT[],
    data TEXT,
    event_name VARCHAR(100),
    event_signature VARCHAR(66),
    removed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 添加注释
COMMENT ON TABLE event_logs IS '事件日志表 - 存储智能合约事件日志';
COMMENT ON COLUMN event_logs.id IS '主键ID - 自增唯一标识';
COMMENT ON COLUMN event_logs.chain_id IS '区块链网络标识';
COMMENT ON COLUMN event_logs.transaction_id IS '交易ID - 关联transactions.id';
COMMENT ON COLUMN event_logs.block_header_id IS '区块头ID - 关联block_headers.id';
COMMENT ON COLUMN event_logs.log_index IS '日志索引 - 在交易中的日志位置索引';
COMMENT ON COLUMN event_logs.block_number IS '区块高度 - 事件所在区块高度';
COMMENT ON COLUMN event_logs.transaction_hash IS '交易哈希 - 产生事件的交易哈希';
COMMENT ON COLUMN event_logs.address IS '合约地址 - 发出事件的合约地址';
COMMENT ON COLUMN event_logs.topics IS '主题数组 - 事件签名和索引参数，第一个topic是事件签名';
COMMENT ON COLUMN event_logs.data IS '事件数据 - 事件的非索引参数数据';
COMMENT ON COLUMN event_logs.event_name IS '事件名称 - 解析后的事件名称';
COMMENT ON COLUMN event_logs.event_signature IS '事件签名 - 事件签名的keccak哈希';
COMMENT ON COLUMN event_logs.removed IS '是否被移除 - 日志是否因链重组被移除';
COMMENT ON COLUMN event_logs.created_at IS '创建时间 - 记录插入时间';

-- 添加约束和索引
ALTER TABLE event_logs ADD CONSTRAINT fk_event_logs_transaction 
    FOREIGN KEY (transaction_id) REFERENCES transactions(id) ON DELETE CASCADE;
ALTER TABLE event_logs ADD CONSTRAINT fk_event_logs_block_header 
    FOREIGN KEY (block_header_id) REFERENCES block_headers(id) ON DELETE CASCADE;
ALTER TABLE event_logs ADD CONSTRAINT uq_event_logs_chain_tx_log UNIQUE (chain_id, transaction_hash, log_index);

CREATE INDEX idx_event_logs_chain_block ON event_logs(chain_id, block_number);
CREATE INDEX idx_event_logs_address ON event_logs(address);
CREATE INDEX idx_event_logs_tx_hash ON event_logs(transaction_hash);
CREATE INDEX idx_event_logs_tx_id ON event_logs(transaction_id);
CREATE INDEX idx_event_logs_block_id ON event_logs(block_header_id);
CREATE INDEX idx_event_logs_event_name ON event_logs(event_name);
CREATE INDEX idx_event_logs_topics ON event_logs USING GIN(topics);