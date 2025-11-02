package com.web3.indexer.model.dto;

import java.io.Serializable;
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
 * 事件日志实体类
 * 存储智能合约事件日志，包含事件签名、参数数据和关联信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("event_logs")
public class EventLog implements Serializable {

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
     * 交易ID - 关联transactions.id
     */
    @TableField(value = "transaction_id")
    private Long transactionId;

    /**
     * 区块头ID - 关联block_headers.id
     */
    @TableField(value = "block_header_id")
    private Long blockHeaderId;

    /**
     * 日志索引 - 在交易中的日志位置索引
     */
    @TableField(value = "log_index")
    private Integer logIndex;

    /**
     * 区块高度 - 事件所在区块高度
     */
    @TableField(value = "block_number")
    private Long blockNumber;

    /**
     * 交易哈希 - 产生事件的交易哈希
     */
    @TableField(value = "transaction_hash")
    private String transactionHash;

    /**
     * 合约地址 - 发出事件的合约地址
     */
    @TableField("address")
    private String address;

    /**
     * 主题数组 - 事件签名和索引参数，第一个topic是事件签名
     * 存储为JSON字符串格式
     */
    @TableField(value = "topics")
    private String topics;

    /**
     * 事件数据 - 事件的非索引参数数据
     */
    @TableField("data")
    private String data;

    /**
     * 事件名称 - 解析后的事件名称
     */
    @TableField(value = "event_name")
    private String eventName;

    /**
     * 事件签名 - 事件签名的keccak哈希
     */
    @TableField(value = "event_signature")
    private String eventSignature;

    /**
     * 是否被移除 - 日志是否因链重组被移除
     */
    @TableField("removed")
    private Boolean removed = false;

    /**
     * 创建时间 - 记录插入时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
