package com.web3.indexer.service;

import java.util.List;

import com.web3.indexer.model.dto.IndexerStatusResponse;

public interface ChainindexerService {

    public IndexerStatusResponse indexerStatusByChain(String chainId);

    public List<IndexerStatusResponse> indexerStatusAll();

}
