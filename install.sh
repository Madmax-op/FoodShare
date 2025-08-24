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
