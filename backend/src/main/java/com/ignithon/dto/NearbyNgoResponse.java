package com.ignithon.dto;

public class NearbyNgoResponse {
    private Long id;
    private String name;
    private String organizationType;
    private String description;
    private String city;
    private Double latitude;
    private Double longitude;
    private Double distance; // in kilometers
    private boolean acceptsDonations;
    private int maxDonationDistance;
    private int maxDonationQuantity;
    
    // Constructors
    public NearbyNgoResponse() {}
    
    public NearbyNgoResponse(Long id, String name, String organizationType, String description, 
                           String city, Double latitude, Double longitude, Double distance,
                           boolean acceptsDonations, int maxDonationDistance, int maxDonationQuantity) {
        this.id = id;
        this.name = name;
        this.organizationType = organizationType;
        this.description = description;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.acceptsDonations = acceptsDonations;
        this.maxDonationDistance = maxDonationDistance;
        this.maxDonationQuantity = maxDonationQuantity;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getOrganizationType() { return organizationType; }
    public void setOrganizationType(String organizationType) { this.organizationType = organizationType; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    
    public Double getDistance() { return distance; }
    public void setDistance(Double distance) { this.distance = distance; }
    
    public boolean isAcceptsDonations() { return acceptsDonations; }
    public void setAcceptsDonations(boolean acceptsDonations) { this.acceptsDonations = acceptsDonations; }
    
    public int getMaxDonationDistance() { return maxDonationDistance; }
    public void setMaxDonationDistance(int maxDonationDistance) { this.maxDonationDistance = maxDonationDistance; }
    
    public int getMaxDonationQuantity() { return maxDonationQuantity; }
    public void setMaxDonationQuantity(int maxDonationQuantity) { this.maxDonationQuantity = maxDonationQuantity; }
}
