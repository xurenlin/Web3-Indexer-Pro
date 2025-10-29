package com.web3.indexer.component.health.checker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import com.web3.indexer.component.health.HealthChecker;
import com.web3.indexer.component.health.model.ServiceHealthStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MysqlHealthChecker implements HealthChecker {

    private static final Logger logger = LoggerFactory.getLogger(MysqlHealthChecker.class);

    @Value("${health.services.mysql.service-name:mysql}")
    private String servicename;

    // 直接从配置文件中注入参数
    @Value("${health.services.mysql.endpoint:jdbc:mysql://localhost:3306/mysql}")
    private String url;

    @Value("${health.services.mysql.username:root}")
    private String username;

    @Value("${health.services.mysql.password:}")
    private String password;

    @Value("${health.services.mysql.check-command:SELECT 1}")
    private String checkCommand;

    @Override
    public ServiceHealthStatus check() {
        long startTime = System.currentTimeMillis();

        // 每次都创建新连接，用完立即关闭
        try (Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement()) {

            // 执行健康检查查询
            boolean isValid = statement.execute(checkCommand);
            long duration = System.currentTimeMillis() - startTime;

            if (isValid) {
                logger.debug("MySQL health check successful");
                return ServiceHealthStatus.healthy(
                        getServiceName(),
                        "MySQL database is healthy",
                        duration);
            } else {
                logger.warn("MySQL health check query failed");
                return ServiceHealthStatus.unhealthy(
                        getServiceName(),
                        "Health check query returned no result",
                        duration);
            }

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("MySQL health check failed for URL: {}", url, e);
            return ServiceHealthStatus.unhealthy(
                    getServiceName(),
                    "Database connection failed: " + e.getMessage(),
                    duration);
        }
    }

    @Override
    public String getServiceName() {
        return servicename;
    }

    @Override
    public boolean isEnabled() {
        return url != null && !url.trim().isEmpty();
    }
}