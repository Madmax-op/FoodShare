package com.ignithon.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "donations")
@EntityListeners(AuditingEntityListener.class)
public class Donation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Donor is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id", nullable = false)
    private Donor donor;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ngo_id")
    private NGO ngo;
    
    @NotBlank(message = "Food details are required")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String foodDetails;
    
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    @Column(nullable = false)
    private Double quantity; // in kg
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DonationStatus status = DonationStatus.PENDING;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FoodType foodType;
    
    @Column(columnDefinition = "TEXT")
    private String specialInstructions;
    
    @Column(nullable = false)
    private LocalDateTime expiryTime;
    
    @Column(nullable = false)
    private LocalDateTime pickupTime;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    // Constructors
    public Donation() {}
    
    public Donation(Donor donor, String foodDetails, Double quantity, FoodType foodType, 
                   LocalDateTime expiryTime, LocalDateTime pickupTime) {
        this.donor = donor;
        this.foodDetails = foodDetails;
        this.quantity = quantity;
        this.foodType = foodType;
        this.expiryTime = expiryTime;
        this.pickupTime = pickupTime;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Donor getDonor() { return donor; }
    public void setDonor(Donor donor) { this.donor = donor; }
    
    public NGO getNgo() { return ngo; }
    public void setNgo(NGO ngo) { this.ngo = ngo; }
    
    public String getFoodDetails() { return foodDetails; }
    public void setFoodDetails(String foodDetails) { this.foodDetails = foodDetails; }
    
    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }
    
    public DonationStatus getStatus() { return status; }
    public void setStatus(DonationStatus status) { this.status = status; }
    
    public FoodType getFoodType() { return foodType; }
    public void setFoodType(FoodType foodType) { this.foodType = foodType; }
    
    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }
    
    public LocalDateTime getExpiryTime() { return expiryTime; }
    public void setExpiryTime(LocalDateTime expiryTime) { this.expiryTime = expiryTime; }
    
    public LocalDateTime getPickupTime() { return pickupTime; }
    public void setPickupTime(LocalDateTime pickupTime) { this.pickupTime = pickupTime; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // Enums
    public enum DonationStatus {
        PENDING, ACCEPTED, REJECTED, PICKED_UP, EXPIRED, COMPLETED
    }
    
    public enum FoodType {
        COOKED_MEAL, FRESH_FRUITS, VEGETABLES, BREAD_BAKERY, DAIRY_PRODUCTS, 
        PACKAGED_FOOD, LEFTOVERS, OTHER
    }
} 