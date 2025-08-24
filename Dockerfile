# Multi-stage build for FoodShare application
FROM maven:3.8.4-openjdk-17 AS backend-builder

# Set working directory
WORKDIR /app

# Copy backend source code
COPY backend/ ./backend/

# Build the backend
RUN cd backend && mvn clean package -DskipTests

# Python ML service stage
FROM python:3.9-slim AS ml-builder

WORKDIR /app

# Copy ML service requirements and source
COPY ml-prediction/requirements.txt .
COPY ml-prediction/app.py .

# Install dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Final production stage
FROM eclipse-temurin:17-jre-jammy

# Install system dependencies
RUN apt-get update && apt-get install -y \
    nginx \
    python3 \
    python3-pip \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy built backend JAR
COPY --from=backend-builder /app/backend/target/food-donation-system-1.0.0.jar ./backend/app.jar

# Copy ML service
COPY --from=ml-builder /app/app.py ./ml-service/app.py
COPY --from=ml-builder /usr/local/lib/python3.9/site-packages ./ml-service/site-packages

# Copy frontend files
COPY frontend/ ./frontend/

# Copy nginx configuration
COPY nginx.conf /etc/nginx/nginx.conf

# Copy startup script
COPY start-production.sh ./start-production.sh
RUN chmod +x ./start-production.sh

# Create necessary directories
RUN mkdir -p /var/log/nginx /var/cache/nginx

# Expose ports
EXPOSE 8080 5000 80

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/api/health || exit 1

# Start the application
CMD ["./start-production.sh"]
