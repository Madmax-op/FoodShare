# 📧 Email Service Setup Guide

This guide explains how to configure the SMTP email service for the FoodShare application.

## ✅ Current Configuration

The email service is already configured with **Gmail SMTP** in `application.properties`:

```properties
# Mail Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${MAIL_USERNAME:your-email@gmail.com}
spring.mail.password=${MAIL_PASSWORD:your-app-password}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.transport.protocol=smtp
spring.mail.properties.mail.debug=false

# Email Service Configuration
app.email.enabled=true
app.email.from=${MAIL_USERNAME:your-email@gmail.com}
app.email.welcome-template-enabled=true
app.email.notification-enabled=true
```

## 🔧 Gmail SMTP Setup

### 1. Enable 2-Factor Authentication
1. Go to your Google Account settings
2. Navigate to Security
3. Enable 2-Step Verification

### 2. Generate App Password
1. Go to Google Account settings
2. Navigate to Security → 2-Step Verification
3. Click on "App passwords"
4. Generate a new app password for "Mail"
5. Copy the 16-character password

### 3. Update Environment Variables
Add these to your `.env` file:

```bash
# Email Configuration (Gmail)
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-16-character-app-password
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_FROM=your-email@gmail.com
```

## 🧪 Testing Email Service

### 1. Test Email Health
```bash
curl http://localhost:8080/api/test/email/health
```

### 2. Test Welcome Email
```bash
curl -X POST http://localhost:8080/api/test/email/welcome \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "test@example.com",
    "phone": "+1234567890",
    "city": "New York",
    "role": "DONOR"
  }'
```

### 3. Test Donation Notification
```bash
curl -X POST http://localhost:8080/api/test/email/donation-notification \
  -H "Content-Type: application/json" \
  -d '{
    "ngoEmail": "ngo@example.com",
    "donorName": "John Doe",
    "foodDetails": "Fresh vegetables and bread",
    "quantity": "10 kg"
  }'
```

## 📧 Email Templates

The application includes these email templates:

### 1. Welcome Email
- Sent to new users upon registration
- Includes user details and role-specific information
- Provides login link and next steps

### 2. Donation Notification
- Sent to NGOs when new donations are available
- Includes donor details and food information
- Provides pickup instructions

### 3. Password Reset
- Sent when users request password reset
- Includes reset token and instructions

## 🔄 Email Service Features

### Automatic Email Sending
- ✅ Welcome emails on user registration
- ✅ Donation notifications to NGOs
- ✅ Password reset emails
- ✅ Error handling (doesn't break registration if email fails)

### Email Content
- ✅ Professional formatting
- ✅ Role-specific information
- ✅ Clear call-to-action buttons
- ✅ Contact information

## 🚨 Troubleshooting

### Common Issues

1. **Authentication Failed**:
   ```
   Error: 535-5.7.8 Username and Password not accepted
   ```
   **Solution**: Use App Password instead of regular password

2. **Connection Timeout**:
   ```
   Error: Connection timed out
   ```
   **Solution**: Check firewall settings and port 587

3. **SSL/TLS Issues**:
   ```
   Error: SSL handshake failed
   ```
   **Solution**: Ensure STARTTLS is enabled

### Debug Mode

Enable email debugging in `application.properties`:
```properties
spring.mail.properties.mail.debug=true
spring.mail.properties.mail.smtp.debug=true
```

### Test Different SMTP Providers

#### Outlook/Hotmail
```properties
spring.mail.host=smtp-mail.outlook.com
spring.mail.port=587
spring.mail.username=your-email@outlook.com
spring.mail.password=your-password
```

#### Yahoo Mail
```properties
spring.mail.host=smtp.mail.yahoo.com
spring.mail.port=587
spring.mail.username=your-email@yahoo.com
spring.mail.password=your-app-password
```

#### Custom SMTP Server
```properties
spring.mail.host=your-smtp-server.com
spring.mail.port=587
spring.mail.username=your-username
spring.mail.password=your-password
```

## 📋 Email Service Checklist

- [ ] Gmail account with 2FA enabled
- [ ] App password generated
- [ ] Environment variables set in `.env`
- [ ] Email service enabled (`app.email.enabled=true`)
- [ ] Test email sent successfully
- [ ] Welcome emails working on registration
- [ ] Donation notifications working

## 🔒 Security Considerations

1. **Never commit email passwords to version control**
2. **Use environment variables for sensitive data**
3. **Enable 2FA on email accounts**
4. **Use app passwords instead of regular passwords**
5. **Monitor email sending logs**

## 📊 Email Service Monitoring

### Logs to Monitor
```bash
# Email service logs
tail -f backend/logs/application.log | grep EmailService

# SMTP connection logs
tail -f backend/logs/application.log | grep smtp
```

### Health Check
```bash
# Check email service status
curl http://localhost:8080/api/test/email/health
```

## 🎯 Integration with Registration

The email service is automatically integrated with user registration:

```java
// In AuthService.java
public Donor registerDonor(DonorRegistrationRequest request) {
    // ... registration logic ...
    
    Donor savedDonor = donorRepository.save(donor);
    
    // Send welcome email
    try {
        emailService.sendWelcomeEmail(savedDonor);
        logger.info("Welcome email sent to Donor: {}", savedDonor.getEmail());
    } catch (Exception e) {
        logger.warn("Failed to send welcome email to Donor: {}", savedDonor.getEmail(), e);
    }
    
    return savedDonor;
}
```

## 📈 Email Analytics (Future Enhancement)

Consider adding these features:
- Email delivery tracking
- Open rate monitoring
- Click tracking
- Bounce handling
- Email templates management

---

**Note**: The email service is fully functional and integrated with the FoodShare application. Users will automatically receive welcome emails upon registration, and NGOs will receive notifications about new donations.
