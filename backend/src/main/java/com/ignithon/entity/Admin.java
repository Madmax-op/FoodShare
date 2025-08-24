package com.ignithon.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "admins")
public class Admin extends User {
    
    @Column(nullable = false)
    private boolean isSuperAdmin = false;
    
    @Column(nullable = false)
    private String adminLevel = "ADMIN"; // ADMIN, SUPER_ADMIN
    
    // Constructors
    public Admin() {
        super();
        setRole(UserRole.ADMIN);
    }
    
    public Admin(String name, String email, String phone, String city, Double latitude, Double longitude, 
                 String password) {
        super(name, email, phone, city, latitude, longitude, password, UserRole.ADMIN);
    }
    
    public Admin(String name, String email, String phone, String city, Double latitude, Double longitude, 
                 String password, boolean isSuperAdmin, String adminLevel) {
        super(name, email, phone, city, latitude, longitude, password, UserRole.ADMIN);
        this.isSuperAdmin = isSuperAdmin;
        this.adminLevel = adminLevel;
    }
    
    // Getters and Setters
    public boolean isSuperAdmin() { return isSuperAdmin; }
    public void setSuperAdmin(boolean superAdmin) { isSuperAdmin = superAdmin; }
    
    public String getAdminLevel() { return adminLevel; }
    public void setAdminLevel(String adminLevel) { this.adminLevel = adminLevel; }
}
