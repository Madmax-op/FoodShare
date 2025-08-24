package com.ignithon.dto;

import com.ignithon.entity.User;
import jakarta.validation.constraints.*;

public class SimpleRegistrationRequest {
    
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
    private Double latitude;
    
    @NotNull(message = "Longitude is required")
    private Double longitude;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    @NotNull(message = "Role is required")
    private User.UserRole role;
    
    // NGO specific fields
    private String organizationType;
    private String ngoDescription;
    
    // Donor specific fields
    private String donorType;
    private String donorDescription;
    
    // Admin specific fields
    private Boolean isSuperAdmin;
    private String adminLevel;
    
    // Constructors
    public SimpleRegistrationRequest() {}
    
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
    
    public String getOrganizationType() { return organizationType; }
    public void setOrganizationType(String organizationType) { this.organizationType = organizationType; }
    
    public String getNgoDescription() { return ngoDescription; }
    public void setNgoDescription(String ngoDescription) { this.ngoDescription = ngoDescription; }
    
    public String getDonorType() { return donorType; }
    public void setDonorType(String donorType) { this.donorType = donorType; }
    
    public String getDonorDescription() { return donorDescription; }
    public void setDonorDescription(String donorDescription) { this.donorDescription = donorDescription; }
    
    public Boolean getIsSuperAdmin() { return isSuperAdmin; }
    public void setIsSuperAdmin(Boolean isSuperAdmin) { this.isSuperAdmin = isSuperAdmin; }
    
    public String getAdminLevel() { return adminLevel; }
    public void setAdminLevel(String adminLevel) { this.adminLevel = adminLevel; }
}
