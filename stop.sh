#!/bin/bash

echo "🛑 Stopping FoodShare Application..."

# Kill processes by port
echo "Stopping services on ports 3000, 5000, 8080..."
pkill -f "python.*3000" 2>/dev/null
pkill -f "python.*app.py" 2>/dev/null
pkill -f "java.*spring-boot" 2>/dev/null

# Kill by PID if files exist
if [ -f .ml_pid ]; then
    kill $(cat .ml_pid) 2>/dev/null
    rm .ml_pid
fi

if [ -f .backend_pid ]; then
    kill $(cat .backend_pid) 2>/dev/null
    rm .backend_pid
fi

if [ -f .frontend_pid ]; then
    kill $(cat .frontend_pid) 2>/dev/null
    rm .frontend_pid
fi

echo "✅ All services stopped."
