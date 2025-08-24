package com.ignithon.dto;

import com.ignithon.entity.Delivery;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DeliveryRegistrationRequest {
    
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
    
    @NotNull(message = "Vehicle type is required")
    private Delivery.VehicleType vehicleType;
    
    @Size(max = 20, message = "License number must not exceed 20 characters")
    private String licenseNumber;
    
    @Size(max = 50, message = "Vehicle model must not exceed 50 characters")
    private String vehicleModel;
    
    @Size(max = 20, message = "Vehicle number must not exceed 20 characters")
    private String vehicleNumber;
    
    @Min(value = 1, message = "Max delivery distance must be at least 1 km")
    @Max(value = 100, message = "Max delivery distance must not exceed 100 km")
    private Integer maxDeliveryDistance = 25;
    
    @Size(max = 500, message = "Delivery areas must not exceed 500 characters")
    private String deliveryAreas;
    
    // Constructors
    public DeliveryRegistrationRequest() {}
    
    public DeliveryRegistrationRequest(String name, String email, String phone, String city, 
                                     Double latitude, Double longitude, String password, 
                                     Delivery.VehicleType vehicleType) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.password = password;
        this.vehicleType = vehicleType;
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
