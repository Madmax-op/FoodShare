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
