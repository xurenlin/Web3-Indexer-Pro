package com.web3.indexer.mapper;

import com.web3.indexer.model.dto.IndexerStatusResponse;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface IndexerStatusMapper {

    @Select("SELECT * FROM indexer_status WHERE chain_id = #{chainId}")
    public IndexerStatusResponse getIndexerStatus(String chainId);

    @Update("UPDATE indexer_status SET status = #{status}, update_time = #{lastUpdated} WHERE chain_id = #{chainId}")
    void updateIndexerStatus(IndexerStatusResponse status);

}
