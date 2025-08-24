package com.ignithon.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "ngos")
public class NGO extends User {
    
    @NotBlank(message = "Organization type is required")
    @Size(max = 100, message = "Organization type must be less than 100 characters")
    @Column(nullable = false)
    private String organizationType;
    
    @Size(max = 500, message = "Description must be less than 500 characters")
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private boolean acceptsDonations = true;
    
    @Column(nullable = false)
    private int maxDonationDistance = 50; // in kilometers
    
    @Column(nullable = false)
    private int maxDonationQuantity = 100; // in kg
    
    // Constructors
    public NGO() {
        super();
        setRole(UserRole.NGO);
    }
    
    public NGO(String name, String email, String phone, String city, Double latitude, Double longitude, 
               String password, String organizationType, String description) {
        super(name, email, phone, city, latitude, longitude, password, UserRole.NGO);
        this.organizationType = organizationType;
        this.description = description;
    }
    
    // Getters and Setters
    public String getOrganizationType() { return organizationType; }
    public void setOrganizationType(String organizationType) { this.organizationType = organizationType; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public boolean isAcceptsDonations() { return acceptsDonations; }
    public void setAcceptsDonations(boolean acceptsDonations) { this.acceptsDonations = acceptsDonations; }
    
    public int getMaxDonationDistance() { return maxDonationDistance; }
    public void setMaxDonationDistance(int maxDonationDistance) { this.maxDonationDistance = maxDonationDistance; }
    
    public int getMaxDonationQuantity() { return maxDonationQuantity; }
    public void setMaxDonationQuantity(int maxDonationQuantity) { this.maxDonationQuantity = maxDonationQuantity; }
} 