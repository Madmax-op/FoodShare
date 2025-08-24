#!/bin/bash

# Production startup script for FoodShare
set -e

echo "ðŸš€ Starting FoodShare Production Environment..."

# Function to wait for service to be ready
wait_for_service() {
    local service_name=$1
    local service_url=$2
    local max_attempts=30
    local attempt=1
    
    echo "â³ Waiting for $service_name to be ready..."
    
    while [ $attempt -le $max_attempts ]; do
        if curl -s "$service_url" > /dev/null 2>&1; then
            echo "âœ… $service_name is ready!"
            return 0
        fi
        
        echo "   Attempt $attempt/$max_attempts - $service_name not ready yet..."
        sleep 2
        attempt=$((attempt + 1))
    done
    
    echo "âŒ $service_name failed to start after $max_attempts attempts"
    return 1
}

# Start nginx in background
echo "ðŸŒ Starting Nginx..."
nginx -g "daemon off;" &
NGINX_PID=$!

# Start ML service in background
echo "ðŸ¤– Starting ML Service..."
cd /app/ml-service
python3 app.py &
ML_PID=$!

# Wait for ML service to be ready
wait_for_service "ML Service" "http://localhost:5000/health"

# Start backend service in background
echo "â˜• Starting Spring Boot Backend..."
cd /app/backend
java -jar app.jar &
BACKEND_PID=$!

# Wait for backend to be ready
wait_for_service "Backend" "http://localhost:8080/api/health"

echo "ðŸŽ‰ All services started successfully!"
echo "ðŸ“Š Frontend: http://localhost:80"
echo "ðŸ”§ Backend API: http://localhost:8080"
echo "ðŸ¤– ML Service: http://localhost:5000"

# Function to handle shutdown
cleanup() {
    echo "ðŸ›‘ Shutting down services..."
    
    if [ ! -z "$BACKEND_PID" ]; then
        kill $BACKEND_PID 2>/dev/null || true
    fi
    
    if [ ! -z "$ML_PID" ]; then
        kill $ML_PID 2>/dev/null || true
    fi
    
    if [ ! -z "$NGINX_PID" ]; then
        nginx -s quit 2>/dev/null || true
    fi
    
    echo "âœ… All services stopped"
    exit 0
}

# Set up signal handlers
trap cleanup SIGTERM SIGINT

# Keep the script running and monitor services
while true; do
    # Check if services are still running
    if ! kill -0 $BACKEND_PID 2>/dev/null; then
        echo "âŒ Backend service stopped unexpectedly"
        exit 1
    fi
    
    if ! kill -0 $ML_PID 2>/dev/null; then
        echo "âŒ ML service stopped unexpectedly"
        exit 1
    fi
    
    if ! kill -0 $NGINX_PID 2>/dev/null; then
        echo "âŒ Nginx stopped unexpectedly"
        exit 1
    fi
    
    sleep 10
done
