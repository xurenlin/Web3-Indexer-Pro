package com.web3.indexer.component.health;

import com.web3.indexer.component.health.model.ServiceHealthStatus;

public interface HealthChecker {

    public ServiceHealthStatus check();

    public String getServiceName();

    public default boolean isEnabled() {
        return true;
    }

    public default long getTimeout() {
        return 5000L;
    }
}
