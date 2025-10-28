package com.web3.indexer.service.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import com.web3.indexer.component.health.HealthChecker;
import com.web3.indexer.component.health.model.HealthStatus;
import com.web3.indexer.component.health.model.ServiceHealthStatus;
import com.web3.indexer.service.HealthService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class HealthServiceImpl implements HealthService {

    @Value("${health.global.thread-pool-size:10}")
    private int threadPoolSize;
    private final List<HealthChecker> healthCheckers;
    private ExecutorService healthCheckExecutor;

    public HealthServiceImpl(List<HealthChecker> healthCheckers) {
        this.healthCheckers = healthCheckers;
    }

    @PostConstruct
    public void init() {
        // 确保线程池大小是有效值
        if (threadPoolSize <= 0) {
            threadPoolSize = 10; // 默认值
        }
        // 在依赖注入完成后初始化线程池
        this.healthCheckExecutor = Executors.newFixedThreadPool(threadPoolSize);
    }

    @Override
    public HealthStatus allHealth() {
        long startTime = System.currentTimeMillis();

        // 获取启用的检查器
        List<HealthChecker> enabledHealthCheckers = healthCheckers.stream()
                .filter(HealthChecker::isEnabled)
                .collect(Collectors.toList());

        // 并行执行健康检查
        List<CompletableFuture<ServiceHealthStatus>> healthStatuses = enabledHealthCheckers.stream()
                .map(healthChecker -> CompletableFuture.supplyAsync(
                        () -> executeCheckerWithTimeout(healthChecker),
                        healthCheckExecutor))
                .collect(Collectors.toList());

        // 等待所有任务完成并收集结果
        List<ServiceHealthStatus> results = healthStatuses.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        // 构建 HealthStatus 对象
        HealthStatus healthStatus = buildHealthStatus(results);
        healthStatus.setTotalDuration(System.currentTimeMillis() - startTime);
        return healthStatus;
    }

    /**
     * 根据检查结果构建 HealthStatus
     */
    private HealthStatus buildHealthStatus(List<ServiceHealthStatus> results) {
        HealthStatus healthStatus = new HealthStatus();
        healthStatus.setTimestamp(System.currentTimeMillis());

        // 统计健康状态
        int upCount = 0;
        int downCount = 0;

        for (ServiceHealthStatus serviceHealth : results) {
            String serviceName = serviceHealth.getName();
            healthStatus.getServices().put(serviceName, serviceHealth);
            // 统计 UP/DOWN 数量
            if ("UP".equals(serviceHealth.getStatus())) {
                upCount++;
            } else {
                downCount++;
            }
            // 如果是网关服务，单独设置
            if ("gateway".equals(serviceName)) {
                healthStatus.setGateway(serviceHealth);
            }
        }
        healthStatus.setUpCount(upCount);
        healthStatus.setDownCount(downCount);
        // 设置整体状态
        if (downCount == 0) {
            healthStatus.setStatus("UP");
        } else if (upCount == 0) {
            healthStatus.setStatus("DOWN");
        } else {
            healthStatus.setStatus("DEGRADED");
        }

        return healthStatus;
    }

    @Override
    public ServiceHealthStatus oneHealth(String serviceName) {
        long startTime = System.currentTimeMillis();
        // 查找指定服务名的检查器
        ServiceHealthStatus result = healthCheckers.stream()
                .filter(HealthChecker::isEnabled)
                .filter(checker -> serviceName.equals(checker.getServiceName()))
                .findFirst()
                .map(this::executeCheckerWithTimeout)
                .orElseGet(() -> {
                    String errorMessage = String.format("Service '%s' not found or disabled", serviceName);
                    return ServiceHealthStatus.unhealthy(serviceName, errorMessage);
                });
        result.setDuration(System.currentTimeMillis() - startTime);
        return result;
    }

    /**
     * 执行健康检查器，包含超时控制
     */
    private ServiceHealthStatus executeCheckerWithTimeout(HealthChecker healthChecker) {
        long checkStartTime = System.currentTimeMillis();
        try {
            ServiceHealthStatus status = healthChecker.check();
            status.setDuration(System.currentTimeMillis() - checkStartTime);
            return status;
        } catch (Exception e) {
            ServiceHealthStatus errorStatus = ServiceHealthStatus.unhealthy(healthChecker.getServiceName(),
                    e.getMessage());
            errorStatus.setDuration(System.currentTimeMillis() - checkStartTime);
            return errorStatus;
        }
    }
}