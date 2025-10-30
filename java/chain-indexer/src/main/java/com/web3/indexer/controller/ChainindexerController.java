package com.web3.indexer.controller;

import com.web3.indexer.common.result.ApiResponse;
import com.web3.indexer.model.dto.IndexerStatusResponse;
import com.web3.indexer.service.ChainindexerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chain-indexer")
public class ChainindexerController {

    @Autowired
    public ChainindexerService service;

    @GetMapping("/indexer/status/{chainId}")
    public ApiResponse<IndexerStatusResponse> indexerStatus(@PathVariable("chainId") String chainId) {
        return ApiResponse.success(service.indexerStatus(chainId));
    }
}
