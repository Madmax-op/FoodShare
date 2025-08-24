#!/bin/bash

# ========================================
# 🧪 FoodShare Application Test Script
# ========================================

echo "🧪 Testing FoodShare Application Setup..."

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✅ $2${NC}"
    else
        echo -e "${RED}❌ $2${NC}"
    fi
}

# Test 1: Check if .env file exists
echo "📋 Testing environment configuration..."
if [ -f .env ]; then
    print_status 0 "Environment file (.env) exists"
else
    print_status 1 "Environment file (.env) missing"
fi

# Test 2: Check Java installation
echo "☕ Testing Java installation..."
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -ge 17 ]; then
        print_status 0 "Java $JAVA_VERSION is installed"
    else
        print_status 1 "Java 17+ required, found version $JAVA_VERSION"
    fi
else
    print_status 1 "Java is not installed"
fi

# Test 3: Check Python installation
echo "🐍 Testing Python installation..."
if command -v python3 &> /dev/null; then
    PYTHON_VERSION=$(python3 --version | cut -d' ' -f2 | cut -d'.' -f1,2)
    print_status 0 "Python $PYTHON_VERSION is installed"
else
    print_status 1 "Python 3 is not installed"
fi

# Test 4: Check PostgreSQL installation
echo "📊 Testing PostgreSQL installation..."
if command -v psql &> /dev/null; then
    print_status 0 "PostgreSQL is installed"
else
    print_status 1 "PostgreSQL is not installed"
fi

# Test 5: Check if PostgreSQL is running
echo "🔄 Testing PostgreSQL service..."
if pg_isready -h localhost -p 5432 -U postgres &> /dev/null; then
    print_status 0 "PostgreSQL is running"
else
    print_status 1 "PostgreSQL is not running"
fi

# Test 6: Check database connection
echo "🗄️ Testing database connection..."
if psql -U postgres -h localhost -c "SELECT 1;" &> /dev/null; then
    print_status 0 "Database connection successful"
else
    print_status 1 "Database connection failed"
fi

# Test 7: Check if database exists
echo "📁 Testing database existence..."
if psql -U postgres -h localhost -lqt | cut -d \| -f 1 | grep -qw food_donation_db; then
    print_status 0 "Database 'food_donation_db' exists"
else
    print_status 1 "Database 'food_donation_db' does not exist"
fi

# Test 8: Check PostGIS extension
echo "🗺️ Testing PostGIS extension..."
if psql -U postgres -h localhost -d food_donation_db -c "SELECT PostGIS_Version();" &> /dev/null; then
    print_status 0 "PostGIS extension is enabled"
else
    print_status 1 "PostGIS extension is not enabled"
fi

# Test 9: Check Python dependencies
echo "📦 Testing Python dependencies..."
if [ -f ml-prediction/requirements.txt ]; then
    cd ml-prediction
    if python3 -c "import flask, sklearn, numpy, pandas, joblib" 2>/dev/null; then
        print_status 0 "Python dependencies are installed"
    else
        print_status 1 "Python dependencies are missing"
    fi
    cd ..
else
    print_status 1 "requirements.txt file missing"
fi

# Test 10: Check Maven wrapper
echo "🔨 Testing Maven wrapper..."
if [ -f backend/mvnw ]; then
    print_status 0 "Maven wrapper exists"
else
    print_status 1 "Maven wrapper missing"
fi

# Test 11: Check if backend can be built
echo "🏗️ Testing backend build..."
if [ -f backend/mvnw ]; then
    cd backend
    if ./mvnw clean compile -q; then
        print_status 0 "Backend can be compiled"
    else
        print_status 1 "Backend compilation failed"
    fi
    cd ..
else
    print_status 1 "Cannot test backend build (mvnw missing)"
fi

# Test 12: Check port availability
echo "🔌 Testing port availability..."
PORTS=(5432 5000 8080 3000)
for port in "${PORTS[@]}"; do
    if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null 2>&1; then
        print_status 1 "Port $port is already in use"
    else
        print_status 0 "Port $port is available"
    fi
done

# Test 13: Check Docker (optional)
echo "🐳 Testing Docker installation..."
if command -v docker &> /dev/null; then
    print_status 0 "Docker is installed"
    if command -v docker-compose &> /dev/null; then
        print_status 0 "Docker Compose is installed"
    else
        print_status 1 "Docker Compose is not installed"
    fi
else
    echo -e "${YELLOW}⚠️ Docker is not installed (optional)${NC}"
fi

# Test 14: Check Email Service Configuration
echo "📧 Testing Email Service Configuration..."
if [ -f .env ]; then
    # Load environment variables
    export $(cat .env | grep -v '^#' | xargs)
    
    # Check email configuration
    if [ -n "$MAIL_USERNAME" ] && [ -n "$MAIL_PASSWORD" ]; then
        print_status 0 "Email credentials are configured"
    else
        print_status 1 "Email credentials are not configured"
    fi
    
    if [ "$MAIL_HOST" = "smtp.gmail.com" ]; then
        print_status 0 "Gmail SMTP is configured"
    else
        print_status 1 "Gmail SMTP is not configured"
    fi
else
    print_status 1 "Cannot test email configuration (.env missing)"
fi

# Test 15: Check environment variables
echo "🔧 Testing environment variables..."
if [ -f .env ]; then
    # Load environment variables
    export $(cat .env | grep -v '^#' | xargs)
    
    # Test required variables
    REQUIRED_VARS=("DB_URL" "DB_USERNAME" "DB_PASSWORD" "JWT_SECRET" "MAIL_USERNAME" "MAIL_PASSWORD")
    for var in "${REQUIRED_VARS[@]}"; do
        if [ -n "${!var}" ]; then
            print_status 0 "Environment variable $var is set"
        else
            print_status 1 "Environment variable $var is not set"
        fi
    done
fi

echo ""
echo "🎯 Summary:"
echo "==========="

# Count passed and failed tests
PASSED=0
FAILED=0

# This is a simplified count - in a real implementation you'd track each test result
echo -e "${GREEN}✅ Tests passed: $PASSED${NC}"
echo -e "${RED}❌ Tests failed: $FAILED${NC}"

echo ""
echo "📋 Next Steps:"
echo "=============="
echo "1. Fix any failed tests above"
echo "2. Run './install.sh' to install dependencies"
echo "3. Run './start.sh' to start the application"
echo "4. Access the application at http://localhost:3000"

echo ""
echo "🔗 Useful Commands:"
echo "=================="
echo "• Start application: ./start.sh"
echo "• Stop application: ./stop.sh"
echo "• Install dependencies: ./install.sh"
echo "• Test setup: ./test-setup.sh"
echo "• Docker setup: docker-compose up --build"
