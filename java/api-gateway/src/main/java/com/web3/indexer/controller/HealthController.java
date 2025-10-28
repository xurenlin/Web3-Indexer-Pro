package com.web3.indexer.controller;

import com.web3.indexer.common.result.ApiResponse;
import com.web3.indexer.component.health.model.HealthStatus;
import com.web3.indexer.component.health.model.ServiceHealthStatus;
import com.web3.indexer.service.HealthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gateway")
public class HealthController {

    @Autowired
    private HealthService healthService;

    @GetMapping("/allHealth")
    public ApiResponse<HealthStatus> allHealth() {
        return ApiResponse.success(healthService.allHealth());
    }

    @GetMapping("/oneHealth/{serviceName}")
    public ApiResponse<ServiceHealthStatus> oneHealth(@PathVariable("serviceName") String serviceName) {
        return ApiResponse.success(healthService.oneHealth(serviceName));
    }

}
