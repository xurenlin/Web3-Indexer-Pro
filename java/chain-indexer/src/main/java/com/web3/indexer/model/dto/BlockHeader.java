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
 * 区块头信息实体类
 * 存储区块链的区块头数据，包含区块的基本信息和元数据
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("block_headers")
public class BlockHeader implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID - 自增唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 区块链网络标识 - 例如: ETH_MAINNET, BSC_TESTNET
     */
    @TableField(value = "chain_id")
    private String chainId;

    /**
     * 区块链网络ID - 标准Chain ID: 1=ETH主网, 56=BSC主网
     */
    @TableField(value = "network_id")
    private Integer networkId;

    /**
     * 区块高度 - 区块在链上的编号
     */
    @TableField(value = "block_number")
    private Long blockNumber;

    /**
     * 区块哈希 - 区块的唯一哈希值，0x开头
     */
    @TableField(value = "block_hash")
    private String blockHash;

    /**
     * 父区块哈希 - 前一个区块的哈希值
     */
    @TableField(value = "parent_hash")
    private String parentHash;

    /**
     * 区块时间戳 - 区块产生的Unix时间戳
     */
    @TableField("timestamp")
    private Long timestamp;

    /**
     * 随机数 - 工作量证明的随机数值
     */
    @TableField("nonce")
    private String nonce;

    /**
     * 挖矿难度 - 当前区块的挖矿难度值
     */
    @TableField("difficulty")
    private BigInteger difficulty;

    /**
     * 累计难度 - 到当前区块为止的总难度
     */
    @TableField(value = "total_difficulty")
    private BigInteger totalDifficulty;

    /**
     * 区块大小 - 区块的字节大小
     */
    @TableField("size")
    private Integer size;

    /**
     * Gas限制 - 区块允许的最大Gas用量
     */
    @TableField(value = "gas_limit")
    private Long gasLimit;

    /**
     * 已用Gas - 区块实际使用的Gas总量
     */
    @TableField(value = "gas_used")
    private Long gasUsed;

    /**
     * 矿工地址 - 打包该区块的矿工地址
     */
    @TableField("miner")
    private String miner;

    /**
     * 额外数据 - 区块中包含的额外数据
     */
    @TableField(value = "extra_data")
    private String extraData;

    /**
     * 是否最终确认 - 区块是否已被最终确认
     */
    @TableField(value = "is_finalized")
    private Boolean isFinalized = false;

    /**
     * 创建时间 - 记录插入时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间 - 记录最后更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}