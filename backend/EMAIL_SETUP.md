# 📧 Email Service Setup Guide

This guide explains how to set up and configure the email service for the FoodShare application.

## 🚀 Features

- **Welcome Emails**: Automatically sent to users upon registration
- **Donation Notifications**: Sent to NGOs when new donations are available
- **Password Reset**: Email support for password recovery
- **Test Endpoints**: Development endpoints to verify email functionality

## ⚙️ Configuration

### 1. Gmail Setup (Recommended for Development)

1. **Enable 2-Factor Authentication** on your Gmail account
2. **Generate App Password**:
   - Go to Google Account Settings
   - Security → 2-Step Verification → App passwords
   - Generate a new app password for "Mail"
3. **Update Environment Variables**:
   ```bash
   export MAIL_USERNAME=your-email@gmail.com
   export MAIL_PASSWORD=your-16-digit-app-password
   ```

### 2. Application Properties

The email configuration is already set up in `application.properties`:

```properties
# Mail Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${MAIL_USERNAME:your-email@gmail.com}
spring.mail.password=${MAIL_PASSWORD:your-app-password}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
```

### 3. Alternative Email Providers

#### Outlook/Hotmail
```properties
spring.mail.host=smtp-mail.outlook.com
spring.mail.port=587
```

#### Yahoo Mail
```properties
spring.mail.host=smtp.mail.yahoo.com
spring.mail.port=587
```

## 🧪 Testing the Email Service

### 1. Health Check
```bash
curl http://localhost:8080/api/test/email/health
```

### 2. Test Welcome Email
```bash
curl -X POST http://localhost:8080/api/test/email/welcome \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "phone": "+1234567890",
    "city": "Test City",
    "role": "DONOR"
  }'
```

### 3. Test Donation Notification
```bash
curl -X POST http://localhost:8080/api/test/email/donation-notification \
  -H "Content-Type: application/json" \
  -d '{
    "ngoEmail": "ngo@example.com",
    "donorName": "Test Restaurant",
    "foodDetails": "Fresh vegetables and bread",
    "quantity": "5 kg"
  }'
```

## 📧 Email Templates

### Welcome Email Content
- Personalized greeting with user's name
- Account details summary
- Role-specific information (NGO vs Donor)
- Login instructions
- Support contact information

### Donation Notification Content
- Donor information
- Food details and quantity
- Call-to-action for pickup arrangement
- Thank you message

## 🔧 Troubleshooting

### Common Issues

1. **Authentication Failed**
   - Verify Gmail username and app password
   - Ensure 2FA is enabled
   - Check if app password is correct

2. **Connection Timeout**
   - Verify internet connection
   - Check firewall settings
   - Ensure port 587 is not blocked

3. **Email Not Sent**
   - Check application logs for errors
   - Verify email service is enabled
   - Check recipient email format

### Debug Mode

Enable email debugging in `application.properties`:
```properties
spring.mail.properties.mail.debug=true
```

### Logs

Check application logs for email-related messages:
```bash
tail -f logs/application.log | grep -i email
```

## 🚀 Production Considerations

1. **Email Provider**: Use a production email service (SendGrid, Amazon SES, etc.)
2. **Rate Limiting**: Implement email sending rate limits
3. **Queue System**: Use message queues for reliable email delivery
4. **Monitoring**: Set up email delivery monitoring and alerts
5. **Templates**: Use HTML email templates for better presentation
6. **Unsubscribe**: Implement unsubscribe mechanisms for notifications

## 📚 Additional Resources

- [Spring Boot Email Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-email)
- [Gmail SMTP Setup](https://support.google.com/mail/answer/7126229)
- [Email Best Practices](https://sendgrid.com/blog/email-delivery-best-practices/)

---

**Note**: The email service is designed to fail gracefully. If emails cannot be sent, the application will continue to function normally, and errors will be logged for debugging purposes. 