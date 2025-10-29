package com.web3.indexer.service.impl;

import com.web3.indexer.model.IndexerStatus;
import com.web3.indexer.service.ChainindexerService;

import org.springframework.stereotype.Service;

@Service
public class ChainindexerServiceIpml implements ChainindexerService {

    @Override
    public IndexerStatus indexerStatus() {
        IndexerStatus status = new IndexerStatus();
        return status;
    }

}
