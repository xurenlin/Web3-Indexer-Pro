package com.web3.indexer.component.health.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class ServiceHealthStatus {

    private String status; // UP, DOWN, UNKNOWN
    private Long duration;
    private String message;
    private long timestamp;
    private Map<String, Object> details = new HashMap<>();
    public String name;

    public static ServiceHealthStatus healthy(String name, String message, long duration) {
        ServiceHealthStatus status = new ServiceHealthStatus();
        status.setName(name);
        status.setStatus("UP");
        status.setMessage(message);
        status.setDuration(duration);
        return status;
    }

    public static ServiceHealthStatus healthy(String message) {
        ServiceHealthStatus status = new ServiceHealthStatus();
        status.setStatus("UP");
        status.setMessage(message);
        return status;
    }

    public static ServiceHealthStatus healthy(String name, String message) {
        ServiceHealthStatus status = new ServiceHealthStatus();
        status.setName(name);
        status.setStatus("UP");
        status.setMessage(message);
        return status;
    }

    public static ServiceHealthStatus unhealthy(String message) {
        ServiceHealthStatus status = new ServiceHealthStatus();
        status.setStatus("DOWN");
        status.setMessage(message);
        return status;
    }

    public static ServiceHealthStatus unhealthy(String name, String message, long duration) {
        ServiceHealthStatus status = new ServiceHealthStatus();
        status.setName(name);
        status.setMessage(message);
        status.setStatus("DOWN");
        status.setDuration(duration);
        return status;
    }

    public static ServiceHealthStatus unhealthy(String name, String message) {
        ServiceHealthStatus status = new ServiceHealthStatus();
        status.setName(name);
        status.setStatus("DOWN");
        status.setMessage(message);
        return status;
    }
}
