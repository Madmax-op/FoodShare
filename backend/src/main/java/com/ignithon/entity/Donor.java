package com.ignithon.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "donors")
public class Donor extends User {
    
    @NotBlank(message = "Donor type is required")
    @Size(max = 100, message = "Donor type must be less than 100 characters")
    @Column(nullable = false)
    private String donorType; // RESTAURANT, EVENT, HOSTEL, INDIVIDUAL, etc.
    
    @Size(max = 500, message = "Description must be less than 500 characters")
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private boolean isActive = true;
    
    @Column(nullable = false)
    private int averageDonationQuantity = 0; // in kg
    
    @Column(nullable = false)
    private int totalDonations = 0;
    
    // Constructors
    public Donor() {
        super();
        setRole(UserRole.DONOR);
    }
    
    public Donor(String name, String email, String phone, String city, Double latitude, Double longitude, 
                 String password, String donorType, String description) {
        super(name, email, phone, city, latitude, longitude, password, UserRole.DONOR);
        this.donorType = donorType;
        this.description = description;
    }
    
    // Getters and Setters
    public String getDonorType() { return donorType; }
    public void setDonorType(String donorType) { this.donorType = donorType; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public int getAverageDonationQuantity() { return averageDonationQuantity; }
    public void setAverageDonationQuantity(int averageDonationQuantity) { this.averageDonationQuantity = averageDonationQuantity; }
    
    public int getTotalDonations() { return totalDonations; }
    public void setTotalDonations(int totalDonations) { this.totalDonations = totalDonations; }
} 