package com.web3.indexer.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

/**
 * Web3J 实例工厂
 */
@Component
public class Web3jFactory {

    private final Map<String, Web3j> web3jInstances = new ConcurrentHashMap<>();

    public Web3j getWeb3j(String rpcEndpoint) {
        return web3jInstances.computeIfAbsent(rpcEndpoint, endpoint -> Web3j.build(new HttpService(endpoint)));
    }

    public Web3j getWeb3j(String chainId, String rpcEndpoint) {
        return web3jInstances.computeIfAbsent(chainId, key -> Web3j.build(new HttpService(rpcEndpoint)));
    }
}
