package com.ignithon.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ignithon.entity.User;
import com.ignithon.service.EmailService;

@RestController
@RequestMapping("/test/email")
public class EmailTestController {

    @Autowired
    private EmailService emailService;

    /**
     * Test endpoint to send a welcome email
     * Only available in development environment
     */
    @PostMapping("/welcome")
    public ResponseEntity<Map<String, String>> testWelcomeEmail(@RequestBody Map<String, String> request) {
        try {
            // Create a test user
            User testUser = new User() {
                @Override
                public User.UserRole getRole() {
                    return User.UserRole.valueOf(request.getOrDefault("role", "DONOR"));
                }
            };
            testUser.setName(request.get("name"));
            testUser.setEmail(request.get("email"));
            testUser.setPhone(request.get("phone"));
            testUser.setCity(request.get("city"));

            // Send welcome email
            emailService.sendWelcomeEmail(testUser);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Welcome email sent successfully");
            response.put("email", testUser.getEmail());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to send welcome email: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Test endpoint to send a donation notification email
     */
    @PostMapping("/donation-notification")
    public ResponseEntity<Map<String, String>> testDonationNotification(@RequestBody Map<String, String> request) {
        try {
            String ngoEmail = request.get("ngoEmail");
            String donorName = request.get("donorName");
            String foodDetails = request.get("foodDetails");
            String quantity = request.get("quantity");

            emailService.sendDonationNotification(ngoEmail, donorName, foodDetails, quantity);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Donation notification email sent successfully");
            response.put("email", ngoEmail);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to send donation notification email: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Health check for email service
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> emailHealthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "Email service is running");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }
} 