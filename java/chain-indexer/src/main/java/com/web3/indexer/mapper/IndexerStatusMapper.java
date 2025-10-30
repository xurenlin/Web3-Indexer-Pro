package com.web3.indexer.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web3.indexer.model.dto.IndexerStatusResponse;

// 不要使用 @Mapper 注解
public interface IndexerStatusMapper extends BaseMapper<IndexerStatusResponse> {

    IndexerStatusResponse getIndexerStatus(String chainId);

    void updateIndexerStatus(IndexerStatusResponse status);

    // 新增的方法
    void insertIndexerStatus(IndexerStatusResponse status);

    void saveOrUpdateIndexerStatus(IndexerStatusResponse status);

    List<IndexerStatusResponse> getAllIndexerStatus();

    boolean existsByChainId(String chainId);

    boolean existsById(Integer id);

    void deleteIndexerStatus(String chainId);

    void deleteIndexerStatusById(Integer id);
}