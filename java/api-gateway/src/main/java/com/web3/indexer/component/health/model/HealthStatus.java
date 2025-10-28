package com.web3.indexer.component.health.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class HealthStatus {

    private String status; // UP, DOWN, DEGRADED
    private long timestamp;
    private long totalDuration;
    private Map<String, ServiceHealth> services = new HashMap<>();
    private int upCount;
    private int downCount;
    private ServiceHealth gateway;
}
