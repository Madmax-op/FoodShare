package com.ignithon.dto;

import com.ignithon.entity.Delivery;
import com.ignithon.entity.User;
import com.ignithon.entity.Volunteer;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UnifiedRegistrationRequest {
    
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
    
    @NotNull(message = "Role is required")
    private User.UserRole role;
    
    // NGO specific fields
    @Size(max = 500, message = "NGO description must not exceed 500 characters")
    private String ngoDescription;
    
    @Size(max = 100, message = "NGO type must not exceed 100 characters")
    private String ngoType;
    
    @Size(max = 20, message = "Registration number must not exceed 20 characters")
    private String registrationNumber;
    
    // Donor specific fields
    @Size(max = 100, message = "Donor type must not exceed 100 characters")
    private String donorType;
    
    @Size(max = 500, message = "Donor description must not exceed 500 characters")
    private String donorDescription;
    
    // Volunteer specific fields
    private Volunteer.VolunteerType volunteerType;
    
    @Size(max = 500, message = "Skills must not exceed 500 characters")
    private String skills;
    
    @Size(max = 500, message = "Availability schedule must not exceed 500 characters")
    private String availabilitySchedule;
    
    @Min(value = 1, message = "Max pickup distance must be at least 1 km")
    @Max(value = 50, message = "Max pickup distance must not exceed 50 km")
    private Integer maxPickupDistance;
    
    // Delivery specific fields
    private Delivery.VehicleType vehicleType;
    
    @Size(max = 20, message = "License number must not exceed 20 characters")
    private String licenseNumber;
    
    @Size(max = 50, message = "Vehicle model must not exceed 50 characters")
    private String vehicleModel;
    
    @Size(max = 20, message = "Vehicle number must not exceed 20 characters")
    private String vehicleNumber;
    
    @Min(value = 1, message = "Max delivery distance must be at least 1 km")
    @Max(value = 100, message = "Max delivery distance must not exceed 100 km")
    private Integer maxDeliveryDistance;
    
    @Size(max = 500, message = "Delivery areas must not exceed 500 characters")
    private String deliveryAreas;
    
    // Constructors
    public UnifiedRegistrationRequest() {}
    
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
    
    public User.UserRole getRole() { return role; }
    public void setRole(User.UserRole role) { this.role = role; }
    
    // NGO specific getters and setters
    public String getNgoDescription() { return ngoDescription; }
    public void setNgoDescription(String ngoDescription) { this.ngoDescription = ngoDescription; }
    
    public String getNgoType() { return ngoType; }
    public void setNgoType(String ngoType) { this.ngoType = ngoType; }
    
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    
    // Donor specific getters and setters
    public String getDonorType() { return donorType; }
    public void setDonorType(String donorType) { this.donorType = donorType; }
    
    public String getDonorDescription() { return donorDescription; }
    public void setDonorDescription(String donorDescription) { this.donorDescription = donorDescription; }
    
    // Volunteer specific getters and setters
    public Volunteer.VolunteerType getVolunteerType() { return volunteerType; }
    public void setVolunteerType(Volunteer.VolunteerType volunteerType) { this.volunteerType = volunteerType; }
    
    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }
    
    public String getAvailabilitySchedule() { return availabilitySchedule; }
    public void setAvailabilitySchedule(String availabilitySchedule) { this.availabilitySchedule = availabilitySchedule; }
    
    public Integer getMaxPickupDistance() { return maxPickupDistance; }
    public void setMaxPickupDistance(Integer maxPickupDistance) { this.maxPickupDistance = maxPickupDistance; }
    
    // Delivery specific getters and setters
    public Delivery.VehicleType getVehicleType() { return vehicleType; }
    public void setVehicleType(Delivery.VehicleType vehicleType) { this.vehicleType = vehicleType; }
    
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
}
