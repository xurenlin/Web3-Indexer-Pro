package com.web3.indexer.controller;

import com.web3.indexer.common.result.ApiResponse;
import com.web3.indexer.model.IndexerStatus;
import com.web3.indexer.service.ChainindexerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chain-indexer")
public class ChainindexerController {

    @Autowired
    public ChainindexerService service;

    @RequestMapping("/indexer/status")
    public ApiResponse<IndexerStatus> indexerStatus() {
        return ApiResponse.success(service.indexerStatus());
    }
}
