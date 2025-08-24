#!/bin/bash

echo "🌍 Food Donation Management System - Setup Script"
echo "=================================================="

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker first."
    echo "Visit: https://docs.docker.com/get-docker/"
    exit 1
fi

# Check if Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose is not installed. Please install Docker Compose first."
    echo "Visit: https://docs.docker.com/compose/install/"
    exit 1
fi

echo "✅ Docker and Docker Compose are installed"

# Create necessary directories
echo "📁 Creating project directories..."
mkdir -p ml-prediction/models
mkdir -p database
mkdir -p docker

# Set up environment variables
echo "🔧 Setting up environment variables..."
cat > .env << EOF
# Database Configuration
POSTGRES_DB=food_donation_db
POSTGRES_USER=postgres
POSTGRES_PASSWORD=password

# JWT Configuration
JWT_SECRET=your-super-secret-jwt-key-here-make-it-very-long-and-secure
JWT_EXPIRATION=86400000

# ML Service Configuration
ML_SERVICE_URL=http://localhost:5000

# Email Configuration (optional)
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password

# Twilio Configuration (optional)
TWILIO_ACCOUNT_SID=your-twilio-account-sid
TWILIO_AUTH_TOKEN=your-twilio-auth-token
TWILIO_FROM_NUMBER=your-twilio-phone-number

# Africa's Talking Configuration (optional)
AFRICASTALKING_API_KEY=your-africastalking-api-key
AFRICASTALKING_USERNAME=your-africastalking-username
EOF

echo "✅ Environment variables configured"

# Build and start services
echo "🚀 Building and starting services..."
cd docker
docker-compose up --build -d

# Wait for services to be ready
echo "⏳ Waiting for services to be ready..."
sleep 30

# Check service health
echo "🔍 Checking service health..."

# Check PostgreSQL
if docker exec food_donation_db pg_isready -U postgres > /dev/null 2>&1; then
    echo "✅ PostgreSQL is ready"
else
    echo "❌ PostgreSQL is not ready"
fi

# Check ML Service
if curl -f http://localhost:5000/health > /dev/null 2>&1; then
    echo "✅ ML Service is ready"
else
    echo "❌ ML Service is not ready"
fi

# Check Backend
if curl -f http://localhost:8080/api/health > /dev/null 2>&1; then
    echo "✅ Backend Service is ready"
else
    echo "❌ Backend Service is not ready"
fi

echo ""
echo "🎉 Setup completed!"
echo ""
echo "📱 Access your application:"
echo "   Frontend: http://localhost (if nginx is enabled)"
echo "   Backend API: http://localhost:8080/api"
echo "   ML Service: http://localhost:5000"
echo "   Database: localhost:5432"
echo ""
echo "🔑 Default credentials:"
echo "   Database: postgres/password"
echo ""
echo "📚 Next steps:"
echo "   1. Open frontend/index.html in your browser"
echo "   2. Register as an NGO or Donor"
echo "   3. Test the ML predictions"
echo "   4. Explore the API endpoints"
echo ""
echo "🛠️  Useful commands:"
echo "   View logs: docker-compose logs -f"
echo "   Stop services: docker-compose down"
echo "   Restart services: docker-compose restart"
echo "   View running containers: docker-compose ps"
echo ""
echo "📖 For more information, check the README.md file" 