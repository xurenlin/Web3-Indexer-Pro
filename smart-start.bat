@echo off
chcp 65001 >nul
echo ========================================
echo  Web3 Indexer Pro Service Starter
echo ========================================
echo.

:: Check if docker-compose.yml exists
if not exist "docker-compose.yml" (
    echo ERROR: docker-compose.yml not found in current directory
    pause
    exit /b 1
)

:: Stop any running containers
echo [1/6] Stopping existing containers...
docker-compose down

:: Batch 1: Core databases
echo.
echo [2/6] Starting Batch 1 - Databases...
docker-compose up -d postgres redis
timeout /t 10 /nobreak >nul

:: Batch 2: Message queue
echo.
echo [3/6] Starting Batch 2 - Message Queue...
docker-compose up -d zookeeper
timeout /t 5 /nobreak >nul
docker-compose up -d kafka
timeout /t 10 /nobreak >nul

:: Batch 3: Search and registry
echo.
echo [4/6] Starting Batch 3 - Search & Registry...
docker-compose up -d elasticsearch nacos
timeout /t 10 /nobreak >nul
docker-compose up -d kibana
timeout /t 5 /nobreak >nobreak

:: Batch 4: Monitoring (optional)
echo.
echo [5/6] Starting Batch 4 - Monitoring (Optional)...
docker-compose up -d prometheus grafana zipkin kafka-ui alertmanager node-exporter

:: Final check
echo.
echo [6/6] Checking service status...
echo.
docker-compose ps

echo.
echo ========================================
echo  Services started! Access URLs:
echo.
echo   Nacos:       http://localhost:8848
echo   Kafka UI:    http://localhost:8083  
echo   Grafana:     http://localhost:3000
echo   Kibana:      http://localhost:5601
echo   Prometheus:  http://localhost:9090
echo.
echo   Credentials:
echo   - Nacos:     nacos / nacos@2024
echo   - Grafana:   admin / grafana123
echo   - Redis:     redis123
echo ========================================
echo.

pause