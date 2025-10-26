# Web3 Indexer Pro - Infrastructure Startup
# ASCII Only Version - No encoding issues

[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

Write-Host "Starting Infrastructure Services" -ForegroundColor Cyan
Write-Host ""

try {
    Write-Host "[1/4] Configuring environment..." -ForegroundColor Yellow
    Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process -Force
    
    Write-Host "[2/4] Starting containers..." -ForegroundColor Yellow
    docker compose -f docker-compose.yml up -d
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host "[ERROR] Failed to start containers" -ForegroundColor Red
        exit 1
    }
    
    Write-Host "[3/4] Waiting for services..." -ForegroundColor Yellow
    Start-Sleep -Seconds 10
    
    Write-Host "[4/4] Checking status..." -ForegroundColor Yellow
    
    $runningContainers = docker ps --format "{{.Names}}" 2>$null

    $services = @(
        @{Name = "nacos-server"; Description = "Nacos Registry" },
        @{Name = "mysql-for-nacos"; Description = "MySQL Database" },
        @{Name = "redis-cache"; Description = "Redis Cache" },
        @{Name = "zookeeper"; Description = "Zookeeper" },
        @{Name = "kafka-broker"; Description = "Kafka Service" },
        @{Name = "grafana-dashboard"; Description = "Grafana Monitoring" },
        @{Name = "prometheus-monitor"; Description = "Prometheus" },
        @{Name = "elasticsearch-node"; Description = "Elasticsearch" },
        @{Name = "kibana-dashboard"; Description = "Kibana" },
        @{Name = "postgres-primary"; Description = "PostgreSQL" },
        @{Name = "kafka-ui"; Description = "Kafka Management UI" },
         @{Name = "mongodb-metadata"; Description = "MongoDB Database" },
        @{Name = "mongo-express"; Description = "MongoDB Management UI" }
    )

    foreach ($service in $services) {
        $isRunning = $runningContainers -contains $service.Name
        if ($isRunning) {
            Write-Host "   [OK] $($service.Description)" -ForegroundColor Green
        }
        else {
            Write-Host "   [WARN] $($service.Description) - Not running" -ForegroundColor Yellow
        }
    }
    
    Write-Host ""
    Write-Host "[SUCCESS] Infrastructure started!" -ForegroundColor Green
    
}
catch {
    Write-Host "[ERROR] Failed: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}