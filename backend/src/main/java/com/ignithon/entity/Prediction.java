package com.ignithon.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "predictions")
@EntityListeners(AuditingEntityListener.class)
public class Prediction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Donor is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id", nullable = false)
    private Donor donor;
    
    @NotNull(message = "Predicted quantity is required")
    @Positive(message = "Predicted quantity must be positive")
    @Column(nullable = false)
    private Double predictedQuantity; // in kg
    
    @Column(nullable = false)
    private Double confidence; // ML model confidence score (0.0 to 1.0)
    
    @Column(nullable = false)
    private LocalDateTime predictionDate;
    
    @Column(nullable = false)
    private LocalDateTime validUntil;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PredictionStatus status = PredictionStatus.ACTIVE;
    
    @Column(columnDefinition = "TEXT")
    private String modelVersion;
    
    @Column(columnDefinition = "TEXT")
    private String features; // JSON string of features used for prediction
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // Constructors
    public Prediction() {}
    
    public Prediction(Donor donor, Double predictedQuantity, Double confidence, 
                     LocalDateTime predictionDate, LocalDateTime validUntil, String modelVersion) {
        this.donor = donor;
        this.predictedQuantity = predictedQuantity;
        this.confidence = confidence;
        this.predictionDate = predictionDate;
        this.validUntil = validUntil;
        this.modelVersion = modelVersion;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Donor getDonor() { return donor; }
    public void setDonor(Donor donor) { this.donor = donor; }
    
    public Double getPredictedQuantity() { return predictedQuantity; }
    public void setPredictedQuantity(Double predictedQuantity) { this.predictedQuantity = predictedQuantity; }
    
    public Double getConfidence() { return confidence; }
    public void setConfidence(Double confidence) { this.confidence = confidence; }
    
    public LocalDateTime getPredictionDate() { return predictionDate; }
    public void setPredictionDate(LocalDateTime predictionDate) { this.predictionDate = predictionDate; }
    
    public LocalDateTime getValidUntil() { return validUntil; }
    public void setValidUntil(LocalDateTime validUntil) { this.validUntil = validUntil; }
    
    public PredictionStatus getStatus() { return status; }
    public void setStatus(PredictionStatus status) { this.status = status; }
    
    public String getModelVersion() { return modelVersion; }
    public void setModelVersion(String modelVersion) { this.modelVersion = modelVersion; }
    
    public String getFeatures() { return features; }
    public void setFeatures(String features) { this.features = features; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    // Enum
    public enum PredictionStatus {
        ACTIVE, EXPIRED, USED, INVALID
    }
} 