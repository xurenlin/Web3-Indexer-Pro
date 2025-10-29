package com.web3.indexer.component.health.checker;

import com.web3.indexer.component.health.HealthChecker;
import com.web3.indexer.component.health.model.ServiceHealthStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;

@Component
public class RedisHealthChecker implements HealthChecker {

    private static final Logger logger = LoggerFactory.getLogger(MysqlHealthChecker.class);

    @Value("${health.services.redis.service-name:redis}")
    private String servicename;

    // 直接从配置文件中注入参数
    @Value("${health.services.redis.endpoint:localhost:6379}")
    private String url;

    @Value("${health.services.redis.password:}")
    private String password;

    @Value("${health.services.redis.check-command:PING}")
    private String checkCommand;

    @Value("${health.services.redis.timeout-ms:2000}")
    private int timeout;

    @Override
    public ServiceHealthStatus check() {
        long startTime = System.currentTimeMillis();

        // 解析 endpoint 为 host 和 port
        HostAndPort hostAndPort = parseEndpoint(url);
        if (hostAndPort == null) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("Invalid Redis endpoint format: {}", url);
            return ServiceHealthStatus.unhealthy(
                    getServiceName(),
                    "Invalid endpoint format: " + url,
                    duration);
        }
        // 每次都创建新连接，用完立即关闭
        try (Jedis jedis = new Jedis(hostAndPort.getHost(), hostAndPort.getPort(), timeout)) {

            // 如果有密码，进行认证
            if (password != null && !password.trim().isEmpty()) {
                jedis.auth(password);
            }

            // 执行健康检查查询
            String pingResponse = jedis.ping();
            long duration = System.currentTimeMillis() - startTime;

            if ("PONG".equalsIgnoreCase(pingResponse)) {
                logger.debug("Redis health check successful");
                return ServiceHealthStatus.healthy(
                        getServiceName(),
                        "Redis database is healthy",
                        duration);
            } else {
                logger.warn("Redis health check query failed");
                return ServiceHealthStatus.unhealthy(
                        getServiceName(),
                        "Health check query returned no result",
                        duration);
            }

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("Redis health check failed for URL: {}", url, e);
            return ServiceHealthStatus.unhealthy(
                    getServiceName(),
                    "Redis connection failed: " + e.getMessage(),
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

    /**
     * 解析 endpoint 字符串为 HostAndPort 对象
     * 支持格式: "host:port" 或 "host"
     */
    private HostAndPort parseEndpoint(String endpoint) {
        if (endpoint == null || endpoint.trim().isEmpty()) {
            return null;
        }

        try {
            // 如果包含冒号，解析 host 和 port
            if (endpoint.contains(":")) {
                String[] parts = endpoint.split(":", 2);
                String host = parts[0].trim();
                int port = Integer.parseInt(parts[1].trim());
                return new HostAndPort(host, port);
            } else {
                // 如果不包含冒号，使用默认端口
                return new HostAndPort(endpoint.trim(), 6379);
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid port number in endpoint: {}", endpoint);
            return null;
        } catch (Exception e) {
            logger.error("Failed to parse endpoint: {}", endpoint, e);
            return null;
        }
    }
}