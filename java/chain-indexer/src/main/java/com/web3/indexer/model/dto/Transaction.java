package com.web3.indexer.model.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 交易数据实体类
 * 存储区块链交易信息，包含交易的详细数据和状态信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("transactions")
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID - 自增唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 区块链网络标识
     */
    @TableField(value = "chain_id")
    private String chainId;

    /**
     * 区块头ID - 关联block_headers.id
     */
    @TableField(value = "block_header_id")
    private Long blockHeaderId;

    /**
     * 交易哈希 - 交易的唯一哈希值
     */
    @TableField(value = "transaction_hash")
    private String transactionHash;

    /**
     * 交易索引 - 在区块中的位置索引
     */
    @TableField(value = "transaction_index")
    private Integer transactionIndex;

    /**
     * 区块高度 - 交易所在区块高度
     */
    @TableField(value = "block_number")
    private Long blockNumber;

    /**
     * 发送方地址 - 交易发起者地址
     */
    @TableField(value = "from_address")
    private String fromAddress;

    /**
     * 接收方地址 - 交易接收者地址(合约创建时为null)
     */
    @TableField(value = "to_address")
    private String toAddress;

    /**
     * 合约地址 - 如果是合约创建交易，此为创建的合约地址
     */
    @TableField(value = "contract_address")
    private String contractAddress;

    /**
     * 交易金额 - 转账的ETH/BNB数量(wei单位)
     */
    @TableField("value")
    private BigInteger value;

    /**
     * Gas价格 - 每单位Gas的价格(wei)
     */
    @TableField(value = "gas_price")
    private Long gasPrice;

    /**
     * Gas限制 - 交易允许的最大Gas用量
     */
    @TableField(value = "gas_limit")
    private Long gasLimit;

    /**
     * 实际使用Gas - 交易实际消耗的Gas
     */
    @TableField(value = "gas_used")
    private Long gasUsed;

    /**
     * 最大每Gas费用 - EIP-1559类型交易
     */
    @TableField(value = "max_fee_per_gas")
    private Long maxFeePerGas;

    /**
     * 最大优先费用 - EIP-1559类型交易
     */
    @TableField(value = "max_priority_fee_per_gas")
    private Long maxPriorityFeePerGas;

    /**
     * 输入数据 - 交易调用数据或合约代码
     */
    @TableField(value = "input_data")
    private String inputData;

    /**
     * 交易随机数 - 发送方的交易计数
     */
    @TableField("nonce")
    private Long nonce;

    /**
     * 交易类型 - 0:传统交易, 2:EIP-1559交易
     */
    @TableField(value = "transaction_type")
    private Integer transactionType;

    /**
     * 交易状态 - 1:成功, 0:失败
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间 - 记录插入时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}