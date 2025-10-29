package com.web3.indexer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web3.indexer.model.dto.IndexerStatusResponse;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

// 不要使用 @Mapper 注解
public interface IndexerStatusMapper extends BaseMapper<IndexerStatusResponse> {

    @Select("SELECT * FROM indexer_status WHERE chain_id = #{chainId}")
    IndexerStatusResponse getIndexerStatus(String chainId);

    @Update("UPDATE indexer_status SET status = #{status}, update_time = #{updateTime} WHERE chain_id = #{chainId}")
    void updateIndexerStatus(IndexerStatusResponse status);
}