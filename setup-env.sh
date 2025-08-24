#!/bin/bash

# ========================================
# 🌍 FoodShare Application Setup Script
# ========================================

echo "🚀 Setting up FoodShare Application Environment..."

# Create .env file
cat > .env << 'EOF'
# ========================================
# 🌍 FoodShare Application Environment
# ========================================

# ========================================
# Database Configuration
# ========================================
DB_URL=jdbc:postgresql://localhost:5432/food_donation_db
DB_USERNAME=postgres
DB_PASSWORD=qwer
DB_DRIVER=org.postgresql.Driver

# ========================================
# JWT Configuration
# ========================================
JWT_SECRET=your-super-secret-jwt-key-here-make-it-very-long-and-secure-for-production-use-this-should-be-at-least-256-bits-long
JWT_EXPIRATION=86400000

# ========================================
# Email Configuration (Gmail)
# ========================================
MAIL_USERNAME= vivekaman742@gmail.com
MAIL_PASSWORD= xsia jggq lgyx lhbs
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_FROM=your-email@gmail.com

# ========================================
# Google Maps API Configuration
# ========================================
GOOGLE_MAPS_API_KEY=AIzaSyATGTaalLs0a4UiwkEEe4D1OmGlwcC3W-I
GOOGLE_MAPS_BASE_URL=https://maps.googleapis.com/maps/api

# ========================================
# ML Service Configuration
# ========================================
ML_SERVICE_URL=http://localhost:5000
ML_SERVICE_TIMEOUT=5000

# ========================================
# SMS/Notification Configuration (Twilio)
# ========================================
TWILIO_ACCOUNT_SID=your-twilio-account-sid
TWILIO_AUTH_TOKEN=your-twilio-auth-token
TWILIO_FROM_NUMBER=your-twilio-phone-number

# ========================================
# USSD Configuration (Africa's Talking)
# ========================================
AFRICASTALKING_API_KEY=your-africastalking-api-key
AFRICASTALKING_USERNAME=your-africastalking-username

# ========================================
# Server Configuration
# ========================================
SERVER_PORT=8080
SERVER_CONTEXT_PATH=/api

# ========================================
# CORS Configuration
# ========================================
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:8080,http://127.0.0.1:3000
CORS_ALLOWED_METHODS=GET,POST,PUT,DELETE,OPTIONS
CORS_ALLOWED_HEADERS=*

# ========================================
# Application Features
# ========================================
APP_EMAIL_ENABLED=true
APP_EMAIL_WELCOME_TEMPLATE_ENABLED=true
APP_EMAIL_NOTIFICATION_ENABLED=true
APP_NOTIFICATION_SMS_ENABLED=true
APP_NOTIFICATION_USSD_ENABLED=true

# ========================================
# Logging Configuration
# ========================================
LOG_LEVEL=DEBUG
LOG_LEVEL_SPRING=DEBUG
LOG_LEVEL_HIBERNATE=DEBUG

# ========================================
# Development/Production Mode
# ========================================
SPRING_PROFILES_ACTIVE=dev
NODE_ENV=development
EOF

echo "✅ Created .env file"

# Create ML service requirements.txt
cat > ml-prediction/requirements.txt << 'EOF'
Flask==2.3.3
scikit-learn==1.3.0
numpy==1.24.3
pandas==2.0.3
joblib==1.3.2
requests==2.31.0
python-dotenv==1.0.0
flask-cors==4.0.0
gunicorn==21.2.0
EOF

echo "✅ Created ml-prediction/requirements.txt"

# Create Docker Compose file
cat > docker-compose.yml << 'EOF'
version: '3.8'

services:
  # PostgreSQL Database
  postgres:
    image: postgres:13
    container_name: foodshare-postgres
    environment:
      POSTGRES_DB: food_donation_db
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: qwer
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./database/init.sql:/docker-entrypoint-initdb.d/init.sql
    networks:
      - foodshare-network

  # ML Prediction Service
  ml-service:
    build:
      context: ./ml-prediction
      dockerfile: Dockerfile
    container_name: foodshare-ml-service
    ports:
      - "5000:5000"
    environment:
      - FLASK_ENV=development
    depends_on:
      - postgres
    networks:
      - foodshare-network

  # Spring Boot Backend
  backend:
    build:
      context: ./backend
      dockerfile: Dockerfile
    container_name: foodshare-backend
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
      - DB_URL=jdbc:postgresql://postgres:5432/food_donation_db
      - DB_USERNAME=postgres
      - DB_PASSWORD=qwer
    depends_on:
      - postgres
      - ml-service
    networks:
      - foodshare-network

  # Frontend (Optional)
  frontend:
    image: nginx:alpine
    container_name: foodshare-frontend
    ports:
      - "3000:80"
    volumes:
      - ./frontend:/usr/share/nginx/html
    depends_on:
      - backend
    networks:
      - foodshare-network

volumes:
  postgres_data:

networks:
  foodshare-network:
    driver: bridge
EOF

echo "✅ Created docker-compose.yml"

# Create ML service Dockerfile
cat > ml-prediction/Dockerfile << 'EOF'
FROM python:3.9-slim

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

EXPOSE 5000

CMD ["python", "app.py"]
EOF

echo "✅ Created ml-prediction/Dockerfile"

# Create Backend Dockerfile
cat > backend/Dockerfile << 'EOF'
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
EOF

echo "✅ Created backend/Dockerfile"

# Create database initialization script
cat > database/init.sql << 'EOF'
-- Enable PostGIS extension
CREATE EXTENSION IF NOT EXISTS postgis;

-- Create indexes for spatial queries
CREATE INDEX IF NOT EXISTS idx_users_location ON users USING GIST (ST_MakePoint(longitude, latitude));
CREATE INDEX IF NOT EXISTS idx_ngos_location ON ngos USING GIST (ST_MakePoint(longitude, latitude));
CREATE INDEX IF NOT EXISTS idx_donors_location ON donors USING GIST (ST_MakePoint(longitude, latitude));
CREATE INDEX IF NOT EXISTS idx_volunteers_location ON volunteers USING GIST (ST_MakePoint(longitude, latitude));
EOF

echo "✅ Created database/init.sql"

# Create start script
cat > start.sh << 'EOF'
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
EOF

chmod +x start.sh
echo "✅ Created start.sh"

# Create stop script
cat > stop.sh << 'EOF'
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
EOF

chmod +x stop.sh
echo "✅ Created stop.sh"

# Create install script
cat > install.sh << 'EOF'
#!/bin/bash

echo "🔧 Installing FoodShare Application Dependencies..."

# Check if Java 17 is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 17."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ Java 17 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi

echo "✅ Java version: $(java -version 2>&1 | head -n 1)"

# Check if Python 3 is installed
if ! command -v python3 &> /dev/null; then
    echo "❌ Python 3 is not installed. Please install Python 3.8+."
    exit 1
fi

echo "✅ Python version: $(python3 --version)"

# Check if PostgreSQL is installed
if ! command -v psql &> /dev/null; then
    echo "❌ PostgreSQL is not installed. Please install PostgreSQL."
    exit 1
fi

echo "✅ PostgreSQL version: $(psql --version)"

# Install Python dependencies
echo "📦 Installing Python dependencies..."
cd ml-prediction
pip3 install -r requirements.txt
cd ..

# Build backend
echo "🔨 Building Spring Boot backend..."
cd backend
./mvnw clean install -DskipTests
cd ..

echo "✅ Installation completed!"
echo ""
echo "Next steps:"
echo "1. Update .env file with your configuration"
echo "2. Run ./start.sh to start the application"
echo "3. Access the application at http://localhost:3000"
EOF

chmod +x install.sh
echo "✅ Created install.sh"

# Create README for setup
cat > SETUP_README.md << 'EOF'
# 🚀 FoodShare Application Setup Guide

## Quick Start

1. **Install Dependencies**:
   ```bash
   ./install.sh
   ```

2. **Configure Environment**:
   - Edit `.env` file with your settings
   - Get Google Maps API key from Google Cloud Console
   - Configure email settings (Gmail recommended)

3. **Start Application**:
   ```bash
   ./start.sh
   ```

4. **Access Application**:
   - Frontend: http://localhost:3000
   - Backend API: http://localhost:8080/api
   - Swagger Docs: http://localhost:8080/swagger-ui/index.html

## Manual Setup

### 1. Database Setup
```bash
# Start PostgreSQL
sudo systemctl start postgresql

# Create database
psql -U postgres -c "CREATE DATABASE food_donation_db;"
psql -U postgres -d food_donation_db -c "CREATE EXTENSION IF NOT EXISTS postgis;"
```

### 2. Environment Configuration
Copy `.env.example` to `.env` and update with your values:
- Database credentials
- JWT secret
- Email settings
- Google Maps API key
- SMS/notification settings

### 3. Install Dependencies
```bash
# Python dependencies
cd ml-prediction
pip install -r requirements.txt

# Java dependencies
cd backend
./mvnw clean install
```

### 4. Start Services
```bash
# Start ML service
cd ml-prediction
python app.py &

# Start backend
cd backend
./mvnw spring-boot:run &

# Start frontend
cd frontend
python -m http.server 3000 &
```

## Docker Setup

```bash
# Build and start all services
docker-compose up --build

# Or start individual services
docker-compose up -d postgres
docker-compose up -d ml-service
docker-compose up -d backend
docker-compose up -d frontend
```

## Configuration

### Required Environment Variables

| Variable | Description | Example |
|----------|-------------|---------|
| `DB_URL` | PostgreSQL connection URL | `jdbc:postgresql://localhost:5432/food_donation_db` |
| `DB_USERNAME` | Database username | `postgres` |
| `DB_PASSWORD` | Database password | `qwer` |
| `JWT_SECRET` | JWT signing secret | `your-super-secret-key` |
| `MAIL_USERNAME` | Gmail username | `your-email@gmail.com` |
| `MAIL_PASSWORD` | Gmail app password | `your-app-password` |
| `GOOGLE_MAPS_API_KEY` | Google Maps API key | `your-api-key` |

### Optional Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SERVER_PORT` | Backend server port | `8080` |
| `ML_SERVICE_URL` | ML service URL | `http://localhost:5000` |
| `APP_EMAIL_ENABLED` | Enable email service | `true` |
| `APP_NOTIFICATION_SMS_ENABLED` | Enable SMS notifications | `true` |

## Troubleshooting

### Common Issues

1. **Port Already in Use**:
   ```bash
   # Find process using port
   lsof -i :8080
   
   # Kill process
   kill -9 <PID>
   ```

2. **Database Connection Error**:
   ```bash
   # Check PostgreSQL status
   sudo systemctl status postgresql
   
   # Test connection
   psql -U postgres -d food_donation_db -c "SELECT 1;"
   ```

3. **Java Version Issues**:
   ```bash
   # Check Java version
   java -version
   
   # Should show Java 17+
   ```

4. **Python Dependencies**:
   ```bash
   # Install missing packages
   pip install flask scikit-learn numpy pandas joblib
   ```

### Debug Mode

Enable debug logging by setting in `.env`:
```
LOG_LEVEL=DEBUG
LOG_LEVEL_SPRING=DEBUG
```

## Support

For issues and questions:
1. Check the logs in each service directory
2. Verify all environment variables are set correctly
3. Ensure all required services are running
4. Check network connectivity between services
EOF

echo "✅ Created SETUP_README.md"

echo ""
echo "🎉 Setup completed successfully!"
echo ""
echo "📋 Next steps:"
echo "1. Edit .env file with your configuration"
echo "2. Run ./install.sh to install dependencies"
echo "3. Run ./start.sh to start the application"
echo "4. Access the application at http://localhost:3000"
echo ""
echo "📚 For detailed setup instructions, see SETUP_README.md"
