package com.web3.indexer.service;

import com.web3.indexer.component.health.model.HealthStatus;
import com.web3.indexer.component.health.model.ServiceHealthStatus;

public interface HealthService {

    public HealthStatus allHealth();

    public ServiceHealthStatus oneHealth(String serviceName);

}
