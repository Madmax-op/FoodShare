package com.ignithon.dto;

import com.ignithon.entity.Volunteer;
import jakarta.validation.constraints.*;

public class VolunteerRegistrationRequest {
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Phone is required")
    @Size(min = 10, max = 15, message = "Phone must be between 10 and 15 characters")
    private String phone;
    
    @NotBlank(message = "City is required")
    private String city;
    
    @NotNull(message = "Latitude is required")
    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    private Double latitude;
    
    @NotNull(message = "Longitude is required")
    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    private Double longitude;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    @NotNull(message = "Volunteer type is required")
    private Volunteer.VolunteerType volunteerType;
    
    @Size(max = 500, message = "Skills description must not exceed 500 characters")
    private String skills;
    
    @Size(max = 500, message = "Availability schedule must not exceed 500 characters")
    private String availabilitySchedule;
    
    @Min(value = 1, message = "Maximum pickup distance must be at least 1 km")
    @Max(value = 50, message = "Maximum pickup distance must not exceed 50 km")
    private Integer maxPickupDistance = 10;
    
    // Constructors
    public VolunteerRegistrationRequest() {}
    
    public VolunteerRegistrationRequest(String name, String email, String phone, String city, 
                                     Double latitude, Double longitude, String password, 
                                     Volunteer.VolunteerType volunteerType, String skills, 
                                     String availabilitySchedule, Integer maxPickupDistance) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.password = password;
        this.volunteerType = volunteerType;
        this.skills = skills;
        this.availabilitySchedule = availabilitySchedule;
        this.maxPickupDistance = maxPickupDistance;
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public Volunteer.VolunteerType getVolunteerType() { return volunteerType; }
    public void setVolunteerType(Volunteer.VolunteerType volunteerType) { this.volunteerType = volunteerType; }
    
    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }
    
    public String getAvailabilitySchedule() { return availabilitySchedule; }
    public void setAvailabilitySchedule(String availabilitySchedule) { this.availabilitySchedule = availabilitySchedule; }
    
    public Integer getMaxPickupDistance() { return maxPickupDistance; }
    public void setMaxPickupDistance(Integer maxPickupDistance) { this.maxPickupDistance = maxPickupDistance; }
} 