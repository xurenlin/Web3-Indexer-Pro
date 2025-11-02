-- 创建表
CREATE TABLE block_headers (
    id BIGSERIAL PRIMARY KEY,
    chain_id VARCHAR(50) NOT NULL,
    network_id INTEGER NOT NULL,
    block_number BIGINT NOT NULL,
    block_hash VARCHAR(66) NOT NULL,
    parent_hash VARCHAR(66) NOT NULL,
    timestamp BIGINT NOT NULL,
    nonce VARCHAR(20),
    difficulty NUMERIC(50,0),
    total_difficulty NUMERIC(50,0),
    size INTEGER,
    gas_limit BIGINT,
    gas_used BIGINT,
    miner VARCHAR(42),
    extra_data TEXT,
    is_finalized BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 添加注释
COMMENT ON TABLE block_headers IS '区块头信息表 - 存储区块链的区块头数据';
COMMENT ON COLUMN block_headers.id IS '主键ID - 自增唯一标识';
COMMENT ON COLUMN block_headers.chain_id IS '区块链网络标识 - 例如: ETH_MAINNET, BSC_TESTNET';
COMMENT ON COLUMN block_headers.network_id IS '区块链网络ID - 标准Chain ID: 1=ETH主网, 56=BSC主网';
COMMENT ON COLUMN block_headers.block_number IS '区块高度 - 区块在链上的编号';
COMMENT ON COLUMN block_headers.block_hash IS '区块哈希 - 区块的唯一哈希值，0x开头';
COMMENT ON COLUMN block_headers.parent_hash IS '父区块哈希 - 前一个区块的哈希值';
COMMENT ON COLUMN block_headers.timestamp IS '区块时间戳 - 区块产生的Unix时间戳';
COMMENT ON COLUMN block_headers.nonce IS '随机数 - 工作量证明的随机数值';
COMMENT ON COLUMN block_headers.difficulty IS '挖矿难度 - 当前区块的挖矿难度值';
COMMENT ON COLUMN block_headers.total_difficulty IS '累计难度 - 到当前区块为止的总难度';
COMMENT ON COLUMN block_headers.size IS '区块大小 - 区块的字节大小';
COMMENT ON COLUMN block_headers.gas_limit IS 'Gas限制 - 区块允许的最大Gas用量';
COMMENT ON COLUMN block_headers.gas_used IS '已用Gas - 区块实际使用的Gas总量';
COMMENT ON COLUMN block_headers.miner IS '矿工地址 - 打包该区块的矿工地址';
COMMENT ON COLUMN block_headers.extra_data IS '额外数据 - 区块中包含的额外数据';
COMMENT ON COLUMN block_headers.is_finalized IS '是否最终确认 - 区块是否已被最终确认';
COMMENT ON COLUMN block_headers.created_at IS '创建时间 - 记录插入时间';
COMMENT ON COLUMN block_headers.updated_at IS '更新时间 - 记录最后更新时间';

-- 添加约束和索引
ALTER TABLE block_headers ADD CONSTRAINT uq_block_headers_chain_hash UNIQUE (chain_id, block_hash);
ALTER TABLE block_headers ADD CONSTRAINT uq_block_headers_chain_number UNIQUE (chain_id, block_number);

CREATE INDEX idx_block_headers_chain_block ON block_headers(chain_id, block_number);
CREATE INDEX idx_block_headers_hash ON block_headers(block_hash);
CREATE INDEX idx_block_headers_timestamp ON block_headers(timestamp);
CREATE INDEX idx_block_headers_miner ON block_headers(miner);
CREATE INDEX idx_block_headers_parent ON block_headers(parent_hash);