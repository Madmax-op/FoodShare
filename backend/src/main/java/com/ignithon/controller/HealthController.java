package com.ignithon.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "food-donation-backend");
        health.put("timestamp", LocalDateTime.now().toString());
        health.put("version", "1.0.0");
        
        return ResponseEntity.ok(health);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> serviceInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", "Food Donation Management System");
        info.put("description", "Backend service for food donation platform");
        info.put("version", "1.0.0");
        info.put("java_version", System.getProperty("java.version"));
        info.put("spring_version", "3.2.0");
        info.put("timestamp", LocalDateTime.now().toString());
        
        return ResponseEntity.ok(info);
    }
} 