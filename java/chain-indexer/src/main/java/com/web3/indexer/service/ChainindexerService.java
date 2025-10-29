package com.web3.indexer.service;

import com.web3.indexer.model.dto.IndexerStatusResponse;

public interface ChainindexerService {

    public IndexerStatusResponse indexerStatus(String chainId);

}
