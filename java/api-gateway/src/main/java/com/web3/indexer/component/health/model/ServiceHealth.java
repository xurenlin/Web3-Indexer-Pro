package com.web3.indexer.component.health.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class ServiceHealth {

    private String status; // UP, DOWN, UNKNOWN
    private Long latency;
    private String error;
    private long timestamp;
    private Map<String, Object> details = new HashMap<>();
}
