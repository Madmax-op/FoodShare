package com.ignithon.service;

import com.ignithon.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Autowired
    private JavaMailSender mailSender;
    
    /**
     * Send welcome email to newly registered user
     */
    public void sendWelcomeEmail(User user) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Welcome to FoodShare! 🎉");
            message.setText(createWelcomeEmailContent(user));
            
            mailSender.send(message);
            logger.info("Welcome email sent successfully to: {}", user.getEmail());
            
        } catch (Exception e) {
            logger.error("Failed to send welcome email to: {}", user.getEmail(), e);
            // Don't throw exception - email failure shouldn't prevent registration
        }
    }
    
    /**
     * Send donation notification email to NGO
     */
    public void sendDonationNotification(String ngoEmail, String donorName, String foodDetails, String quantity) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(ngoEmail);
            message.setSubject("New Food Donation Available! 🍽️");
            message.setText(createDonationNotificationContent(donorName, foodDetails, quantity));
            
            mailSender.send(message);
            logger.info("Donation notification email sent successfully to NGO: {}", ngoEmail);
            
        } catch (Exception e) {
            logger.error("Failed to send donation notification email to NGO: {}", ngoEmail, e);
        }
    }
    
    /**
     * Send password reset email
     */
    public void sendPasswordResetEmail(String email, String resetToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Password Reset Request - FoodShare");
            message.setText(createPasswordResetContent(resetToken));
            
            mailSender.send(message);
            logger.info("Password reset email sent successfully to: {}", email);
            
        } catch (Exception e) {
            logger.error("Failed to send password reset email to: {}", email, e);
        }
    }
    
    private String createWelcomeEmailContent(User user) {
        StringBuilder content = new StringBuilder();
        content.append("Dear ").append(user.getName()).append(",\n\n");
        content.append("🎉 Welcome to FoodShare! We're excited to have you on board.\n\n");
        content.append("Your account has been successfully created with the following details:\n");
        content.append("• Name: ").append(user.getName()).append("\n");
        content.append("• Email: ").append(user.getEmail()).append("\n");
        content.append("• Phone: ").append(user.getPhone()).append("\n");
        content.append("• City: ").append(user.getCity()).append("\n");
        content.append("• Role: ").append(user.getRole()).append("\n\n");
        
        if (user.getRole().name().equals("NGO")) {
            content.append("As an NGO, you can now:\n");
            content.append("• Receive notifications about available food donations\n");
            content.append("• Connect with food donors in your area\n");
            content.append("• Manage donation requests and pickups\n");
        } else {
            content.append("As a Donor, you can now:\n");
            content.append("• Post food donations for nearby NGOs\n");
            content.append("• Get predictions for surplus food amounts\n");
            content.append("• Track your donation history\n");
        }
        
        content.append("\n🔗 Get started by logging into your account: http://localhost:3000/login\n\n");
        content.append("If you have any questions, feel free to reach out to our support team.\n\n");
        content.append("Best regards,\nThe FoodShare Team\n");
        content.append("🌍 Making food sharing simple and efficient");
        
        return content.toString();
    }
    
    private String createDonationNotificationContent(String donorName, String foodDetails, String quantity) {
        StringBuilder content = new StringBuilder();
        content.append("🍽️ New Food Donation Available!\n\n");
        content.append("A generous donor has posted a new food donation:\n\n");
        content.append("• Donor: ").append(donorName).append("\n");
        content.append("• Food Details: ").append(foodDetails).append("\n");
        content.append("• Quantity: ").append(quantity).append("\n\n");
        content.append("🔗 Log into your FoodShare account to view details and arrange pickup.\n\n");
        content.append("Thank you for helping reduce food waste and feed those in need!\n\n");
        content.append("Best regards,\nThe FoodShare Team");
        
        return content.toString();
    }
    
    private String createPasswordResetContent(String resetToken) {
        StringBuilder content = new StringBuilder();
        content.append("🔐 Password Reset Request\n\n");
        content.append("You have requested to reset your FoodShare account password.\n\n");
        content.append("Your password reset token is: ").append(resetToken).append("\n\n");
        content.append("🔗 Use this token to reset your password in the app.\n\n");
        content.append("If you didn't request this reset, please ignore this email.\n\n");
        content.append("Best regards,\nThe FoodShare Team");
        
        return content.toString();
    }
} 