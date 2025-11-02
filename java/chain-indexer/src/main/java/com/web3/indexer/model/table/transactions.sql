-- 创建表
CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    chain_id VARCHAR(50) NOT NULL,
    block_header_id BIGINT NOT NULL,
    transaction_hash VARCHAR(66) NOT NULL,
    transaction_index INTEGER NOT NULL,
    block_number BIGINT NOT NULL,
    from_address VARCHAR(42) NOT NULL,
    to_address VARCHAR(42),
    contract_address VARCHAR(42),
    value NUMERIC(50,0),
    gas_price BIGINT,
    gas_limit BIGINT,
    gas_used BIGINT,
    max_fee_per_gas BIGINT,
    max_priority_fee_per_gas BIGINT,
    input_data TEXT,
    nonce BIGINT,
    transaction_type INTEGER,
    status INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 添加注释
COMMENT ON TABLE transactions IS '交易数据表 - 存储区块链交易信息';
COMMENT ON COLUMN transactions.id IS '主键ID - 自增唯一标识';
COMMENT ON COLUMN transactions.chain_id IS '区块链网络标识';
COMMENT ON COLUMN transactions.block_header_id IS '区块头ID - 关联block_headers.id';
COMMENT ON COLUMN transactions.transaction_hash IS '交易哈希 - 交易的唯一哈希值';
COMMENT ON COLUMN transactions.transaction_index IS '交易索引 - 在区块中的位置索引';
COMMENT ON COLUMN transactions.block_number IS '区块高度 - 交易所在区块高度';
COMMENT ON COLUMN transactions.from_address IS '发送方地址 - 交易发起者地址';
COMMENT ON COLUMN transactions.to_address IS '接收方地址 - 交易接收者地址(合约创建时为null)';
COMMENT ON COLUMN transactions.contract_address IS '合约地址 - 如果是合约创建交易，此为创建的合约地址';
COMMENT ON COLUMN transactions.value IS '交易金额 - 转账的ETH/BNB数量(wei单位)';
COMMENT ON COLUMN transactions.gas_price IS 'Gas价格 - 每单位Gas的价格(wei)';
COMMENT ON COLUMN transactions.gas_limit IS 'Gas限制 - 交易允许的最大Gas用量';
COMMENT ON COLUMN transactions.gas_used IS '实际使用Gas - 交易实际消耗的Gas';
COMMENT ON COLUMN transactions.max_fee_per_gas IS '最大每Gas费用 - EIP-1559类型交易';
COMMENT ON COLUMN transactions.max_priority_fee_per_gas IS '最大优先费用 - EIP-1559类型交易';
COMMENT ON COLUMN transactions.input_data IS '输入数据 - 交易调用数据或合约代码';
COMMENT ON COLUMN transactions.nonce IS '交易随机数 - 发送方的交易计数';
COMMENT ON COLUMN transactions.transaction_type IS '交易类型 - 0:传统交易, 2:EIP-1559交易';
COMMENT ON COLUMN transactions.status IS '交易状态 - 1:成功, 0:失败';
COMMENT ON COLUMN transactions.created_at IS '创建时间 - 记录插入时间';

-- 添加约束和索引
ALTER TABLE transactions ADD CONSTRAINT fk_transactions_block_header 
    FOREIGN KEY (block_header_id) REFERENCES block_headers(id) ON DELETE CASCADE;
ALTER TABLE transactions ADD CONSTRAINT uq_transactions_chain_hash UNIQUE (chain_id, transaction_hash);

CREATE INDEX idx_transactions_chain_block ON transactions(chain_id, block_number);
CREATE INDEX idx_transactions_hash ON transactions(transaction_hash);
CREATE INDEX idx_transactions_from ON transactions(from_address);
CREATE INDEX idx_transactions_to ON transactions(to_address);
CREATE INDEX idx_transactions_block_id ON transactions(block_header_id);
CREATE INDEX idx_transactions_contract ON transactions(contract_address);
CREATE INDEX idx_transactions_timestamp ON transactions(created_at);