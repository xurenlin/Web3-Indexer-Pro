package com.web3.indexer.component.health.checker;

import javax.sql.DataSource;

import com.web3.indexer.component.health.HealthChecker;
import com.web3.indexer.component.health.model.ServiceHealth;

public class DatabaseHealthChecker implements HealthChecker {

    private final DataSource dataSource;

    public DatabaseHealthChecker(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ServiceHealth check() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'check'");
    }

    @Override
    public String getServiceName() {
        return "database";
    }

}
