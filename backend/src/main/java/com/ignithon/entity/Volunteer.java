package com.ignithon.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "volunteers")
public class Volunteer extends User {
    
    @NotNull(message = "Volunteer type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VolunteerType volunteerType;
    
    @Column(nullable = false)
    private boolean isAvailable = true;
    
    @Column(nullable = false)
    private Integer points = 0;
    
    @Column(nullable = false)
    private Integer totalPickups = 0;
    
    @Column(nullable = false)
    private Integer totalDeliveries = 0;
    
    @Column(nullable = false)
    private Double averageRating = 0.0;
    
    @Column(nullable = false)
    private Integer totalRatings = 0;
    
    @Column(length = 500)
    private String skills;
    
    @Column(length = 500)
    private String availabilitySchedule;
    
    @Column(nullable = false)
    private Integer maxPickupDistance = 10; // in kilometers
    
    // Constructors
    public Volunteer() {
        super();
        // setRole(UserRole.VOLUNTEER); // Commented out as VOLUNTEER role is removed
    }
    
    public Volunteer(String name, String email, String phone, String city, Double latitude, Double longitude, String password, VolunteerType volunteerType) {
        super(name, email, phone, city, latitude, longitude, password, null); // UserRole.VOLUNTEER removed
        this.volunteerType = volunteerType;
    }
    
    // Getters and Setters
    public VolunteerType getVolunteerType() { return volunteerType; }
    public void setVolunteerType(VolunteerType volunteerType) { this.volunteerType = volunteerType; }
    
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    
    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }
    
    public Integer getTotalPickups() { return totalPickups; }
    public void setTotalPickups(Integer totalPickups) { this.totalPickups = totalPickups; }
    
    public Integer getTotalDeliveries() { return totalDeliveries; }
    public void setTotalDeliveries(Integer totalDeliveries) { this.totalDeliveries = totalDeliveries; }
    
    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }
    
    public Integer getTotalRatings() { return totalRatings; }
    public void setTotalRatings(Integer totalRatings) { this.totalRatings = totalRatings; }
    
    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }
    
    public String getAvailabilitySchedule() { return availabilitySchedule; }
    public void setAvailabilitySchedule(String availabilitySchedule) { this.availabilitySchedule = availabilitySchedule; }
    
    public Integer getMaxPickupDistance() { return maxPickupDistance; }
    public void setMaxPickupDistance(Integer maxPickupDistance) { this.maxPickupDistance = maxPickupDistance; }
    
    // Helper methods
    public void addPoints(Integer points) {
        this.points += points;
    }
    
    public void incrementPickup() {
        this.totalPickups++;
    }
    
    public void incrementDelivery() {
        this.totalDeliveries++;
    }
    
    public void addRating(Integer rating) {
        this.totalRatings++;
        this.averageRating = ((this.averageRating * (this.totalRatings - 1)) + rating) / this.totalRatings;
    }
    
    // Enum for volunteer types
    public enum VolunteerType {
        INDIVIDUAL, CORPORATE_GROUP, STUDENT_GROUP, RELIGIOUS_GROUP, COMMUNITY_GROUP
    }
} 