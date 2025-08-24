package com.ignithon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "deliveries")
public class Delivery extends User {
    
    @NotNull(message = "Vehicle type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType vehicleType;
    
    @Column(nullable = false)
    private boolean isAvailable = true;
    
    @Column(nullable = false)
    private Integer totalDeliveries = 0;
    
    @Column(nullable = false)
    private Double averageRating = 0.0;
    
    @Column(nullable = false)
    private Integer totalRatings = 0;
    
    @Column(length = 20)
    private String licenseNumber;
    
    @Column(length = 50)
    private String vehicleModel;
    
    @Column(length = 20)
    private String vehicleNumber;
    
    @Column(nullable = false)
    private Integer maxDeliveryDistance = 25; // in kilometers
    
    @Column(length = 500)
    private String deliveryAreas;
    
    // Constructors
    public Delivery() {
        super();
        // setRole(UserRole.DELIVERY); // Commented out as DELIVERY role is removed
    }
    
    public Delivery(String name, String email, String phone, String city, Double latitude, Double longitude, String password, VehicleType vehicleType) {
        super(name, email, phone, city, latitude, longitude, password, null); // UserRole.DELIVERY removed
        this.vehicleType = vehicleType;
    }
    
    // Getters and Setters
    public VehicleType getVehicleType() { return vehicleType; }
    public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }
    
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    
    public Integer getTotalDeliveries() { return totalDeliveries; }
    public void setTotalDeliveries(Integer totalDeliveries) { this.totalDeliveries = totalDeliveries; }
    
    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }
    
    public Integer getTotalRatings() { return totalRatings; }
    public void setTotalRatings(Integer totalRatings) { this.totalRatings = totalRatings; }
    
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    
    public String getVehicleModel() { return vehicleModel; }
    public void setVehicleModel(String vehicleModel) { this.vehicleModel = vehicleModel; }
    
    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
    
    public Integer getMaxDeliveryDistance() { return maxDeliveryDistance; }
    public void setMaxDeliveryDistance(Integer maxDeliveryDistance) { this.maxDeliveryDistance = maxDeliveryDistance; }
    
    public String getDeliveryAreas() { return deliveryAreas; }
    public void setDeliveryAreas(String deliveryAreas) { this.deliveryAreas = deliveryAreas; }
    
    // Helper methods
    public void incrementDelivery() {
        this.totalDeliveries++;
    }
    
    public void addRating(Integer rating) {
        this.totalRatings++;
        this.averageRating = ((this.averageRating * (this.totalRatings - 1)) + rating) / this.totalRatings;
    }
    
    // Enum for vehicle types
    public enum VehicleType {
        BIKE, CAR, VAN, TRUCK, AUTO_RICKSHAW
    }
}
