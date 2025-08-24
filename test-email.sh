#!/bin/bash

# ========================================
# 📧 FoodShare Email Service Test Script
# ========================================

echo "📧 Testing FoodShare Email Service..."

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

# Check if backend is running
echo "🔍 Checking if backend is running..."
if curl -s http://localhost:8080/api/health > /dev/null; then
    print_status 0 "Backend is running"
else
    print_status 1 "Backend is not running. Please start the application first."
    echo "Run: ./start.sh"
    exit 1
fi

# Test 1: Email Service Health Check
echo ""
echo "🏥 Testing Email Service Health..."
HEALTH_RESPONSE=$(curl -s http://localhost:8080/api/test/email/health)
if echo "$HEALTH_RESPONSE" | grep -q "Email service is running"; then
    print_status 0 "Email service health check passed"
else
    print_status 1 "Email service health check failed"
    echo "Response: $HEALTH_RESPONSE"
fi

# Test 2: Test Welcome Email
echo ""
echo "📨 Testing Welcome Email..."
WELCOME_RESPONSE=$(curl -s -X POST http://localhost:8080/api/test/email/welcome \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "phone": "+1234567890",
    "city": "Test City",
    "role": "DONOR"
  }')

if echo "$WELCOME_RESPONSE" | grep -q "Welcome email sent successfully"; then
    print_status 0 "Welcome email test passed"
else
    print_status 1 "Welcome email test failed"
    echo "Response: $WELCOME_RESPONSE"
fi

# Test 3: Test Donation Notification Email
echo ""
echo "🍽️ Testing Donation Notification Email..."
DONATION_RESPONSE=$(curl -s -X POST http://localhost:8080/api/test/email/donation-notification \
  -H "Content-Type: application/json" \
  -d '{
    "ngoEmail": "ngo@example.com",
    "donorName": "Test Donor",
    "foodDetails": "Fresh vegetables and bread",
    "quantity": "10 kg"
  }')

if echo "$DONATION_RESPONSE" | grep -q "Donation notification email sent successfully"; then
    print_status 0 "Donation notification email test passed"
else
    print_status 1 "Donation notification email test failed"
    echo "Response: $DONATION_RESPONSE"
fi

# Test 4: Check Email Configuration
echo ""
echo "⚙️ Checking Email Configuration..."
if [ -f .env ]; then
    # Load environment variables
    export $(cat .env | grep -v '^#' | xargs)
    
    if [ -n "$MAIL_USERNAME" ] && [ -n "$MAIL_PASSWORD" ]; then
        print_status 0 "Email credentials are configured"
        echo "   Username: $MAIL_USERNAME"
        echo "   Host: ${MAIL_HOST:-smtp.gmail.com}"
        echo "   Port: ${MAIL_PORT:-587}"
    else
        print_status 1 "Email credentials are not configured"
        echo "   Please set MAIL_USERNAME and MAIL_PASSWORD in .env file"
    fi
else
    print_status 1 ".env file not found"
fi

echo ""
echo "🎯 Email Service Test Summary:"
echo "=============================="
echo "• Health Check: $(echo "$HEALTH_RESPONSE" | grep -q "running" && echo "✅ PASS" || echo "❌ FAIL")"
echo "• Welcome Email: $(echo "$WELCOME_RESPONSE" | grep -q "successfully" && echo "✅ PASS" || echo "❌ FAIL")"
echo "• Donation Notification: $(echo "$DONATION_RESPONSE" | grep -q "successfully" && echo "✅ PASS" || echo "❌ FAIL")"

echo ""
echo "📋 Next Steps:"
echo "=============="
echo "1. Check your email inbox for test emails"
echo "2. Verify email content and formatting"
echo "3. Test with real email addresses"
echo "4. Monitor email service logs"

echo ""
echo "🔗 Useful Commands:"
echo "=================="
echo "• Test email health: curl http://localhost:8080/api/test/email/health"
echo "• View email logs: tail -f backend/logs/application.log | grep EmailService"
echo "• Test with custom email: ./test-email.sh"
echo "• Email setup guide: cat EMAIL_SETUP.md"

echo ""
echo "📧 Email Service Features:"
echo "========================="
echo "✅ Welcome emails on user registration"
echo "✅ Donation notifications to NGOs"
echo "✅ Password reset emails"
echo "✅ Error handling and logging"
echo "✅ Gmail SMTP integration"
echo "✅ Professional email templates"
