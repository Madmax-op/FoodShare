#!/bin/bash

# ========================================
# 🌍 FoodShare Application Start Script
# ========================================

echo "🚀 Starting FoodShare Application..."

# Load environment variables
if [ -f .env ]; then
    export $(cat .env | grep -v '^#' | xargs)
    echo "✅ Loaded environment variables from .env"
else
    echo "⚠️  No .env file found. Using default values."
fi

# Function to check if a port is in use
check_port() {
    if lsof -Pi :$1 -sTCP:LISTEN -t >/dev/null ; then
        echo "❌ Port $1 is already in use"
        return 1
    else
        echo "✅ Port $1 is available"
        return 0
    fi
}

# Check required ports
echo "🔍 Checking port availability..."
check_port 5432 || exit 1
check_port 5000 || exit 1
check_port 8080 || exit 1
check_port 3000 || exit 1

# Start PostgreSQL
echo "📊 Starting PostgreSQL..."
if command -v systemctl &> /dev/null; then
    sudo systemctl start postgresql
    echo "✅ PostgreSQL started via systemctl"
else
    echo "⚠️  systemctl not available. Please start PostgreSQL manually."
fi

# Wait for PostgreSQL to be ready
echo "⏳ Waiting for PostgreSQL to be ready..."
until pg_isready -h localhost -p 5432 -U postgres; do
    echo "⏳ PostgreSQL is not ready yet..."
    sleep 2
done
echo "✅ PostgreSQL is ready!"

# Create database if it doesn't exist
echo "🗄️  Setting up database..."
psql -U postgres -h localhost -c "CREATE DATABASE food_donation_db;" 2>/dev/null || echo "Database already exists"
psql -U postgres -h localhost -d food_donation_db -c "CREATE EXTENSION IF NOT EXISTS postgis;" 2>/dev/null || echo "PostGIS extension already exists"

# Start ML Service
echo "🤖 Starting ML Service..."
cd ml-prediction
python app.py &
ML_PID=$!
cd ..

# Wait for ML service to start
echo "⏳ Waiting for ML service to start..."
sleep 5

# Start Backend
echo "☕ Starting Spring Boot Backend..."
cd backend
./mvnw spring-boot:run &
BACKEND_PID=$!
cd ..

# Wait for backend to start
echo "⏳ Waiting for backend to start..."
sleep 10

# Start Frontend
echo "🌐 Starting Frontend..."
cd frontend
python -m http.server 3000 &
FRONTEND_PID=$!
cd ..

echo ""
echo "🎉 All services started successfully!"
echo ""
echo "📱 Application URLs:"
echo "   Frontend:     http://localhost:3000"
echo "   Backend API:  http://localhost:8080/api"
echo "   ML Service:   http://localhost:5000"
echo "   Swagger Docs: http://localhost:8080/swagger-ui/index.html"
echo ""
echo "🧪 Test endpoints:"
echo "   Health Check: curl http://localhost:8080/api/health"
echo "   ML Health:    curl http://localhost:5000/health"
echo "   Maps Health:  curl http://localhost:8080/api/maps/health"
echo ""
echo "Press Ctrl+C to stop all services..."

# Function to cleanup on exit
cleanup() {
    echo ""
    echo "🛑 Stopping all services..."
    kill $ML_PID $BACKEND_PID $FRONTEND_PID 2>/dev/null
    echo "✅ All services stopped."
    exit 0
}

# Set trap to cleanup on script exit
trap cleanup SIGINT SIGTERM

# Wait for user to stop
wait
