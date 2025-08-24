package com.ignithon.service;

import com.ignithon.entity.Prediction;
import com.ignithon.entity.Donor;
import com.ignithon.repository.PredictionRepository;
import com.ignithon.repository.DonorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class PredictionService {
    
    private static final Logger logger = LoggerFactory.getLogger(PredictionService.class);
    
    @Autowired
    private PredictionRepository predictionRepository;
    
    @Autowired
    private DonorRepository donorRepository;
    
    public List<Map<String, Object>> getDonorPredictions(Long donorId) {
        // This would fetch predictions from the ML service
        // For now, returning mock data
        List<Map<String, Object>> predictions = List.of(
            Map.of(
                "date", LocalDateTime.now().plusDays(1),
                "predictedQuantity", 15.5,
                "confidence", 0.85,
                "foodType", "VEGETABLES",
                "reason", "Weekend event expected"
            ),
            Map.of(
                "date", LocalDateTime.now().plusDays(3),
                "predictedQuantity", 8.2,
                "confidence", 0.72,
                "foodType", "BREAD",
                "reason", "Regular surplus pattern"
            ),
            Map.of(
                "date", LocalDateTime.now().plusDays(7),
                "predictedQuantity", 22.0,
                "confidence", 0.91,
                "foodType", "FRUITS",
                "reason", "Seasonal surplus expected"
            )
        );
        
        return predictions;
    }
    
    public Map<String, Object> getDemandPrediction(String city, String foodType) {
        // This would call the ML service for demand prediction
        // For now, returning mock data
        Map<String, Object> prediction = new HashMap<>();
        prediction.put("city", city);
        prediction.put("foodType", foodType);
        prediction.put("predictedDemand", 45.5);
        prediction.put("confidence", 0.78);
        prediction.put("trend", "INCREASING");
        prediction.put("factors", List.of("Population growth", "Seasonal demand", "Economic factors"));
        prediction.put("generatedAt", LocalDateTime.now());
        
        return prediction;
    }
    
    public List<Map<String, Object>> getHighDemandZones() {
        // This would identify zones with high demand for food donations
        // For now, returning mock data
        return List.of(
            Map.of(
                "city", "New York",
                "latitude", 40.7128,
                "longitude", -74.0060,
                "demandScore", 0.92,
                "population", 8400000,
                "foodInsecurityRate", 0.15
            ),
            Map.of(
                "city", "Los Angeles",
                "latitude", 34.0522,
                "longitude", -118.2437,
                "demandScore", 0.88,
                "population", 4000000,
                "foodInsecurityRate", 0.12
            ),
            Map.of(
                "city", "Chicago",
                "latitude", 41.8781,
                "longitude", -87.6298,
                "demandScore", 0.85,
                "population", 2700000,
                "foodInsecurityRate", 0.18
            )
        );
    }
    
    public Map<String, Object> getSurplusPrediction(Long donorId, String foodType, LocalDateTime date) {
        // This would call the ML service for surplus prediction
        // For now, returning mock data
        Map<String, Object> prediction = new HashMap<>();
        prediction.put("donorId", donorId);
        prediction.put("foodType", foodType);
        prediction.put("date", date);
        prediction.put("predictedSurplus", 18.5);
        prediction.put("confidence", 0.82);
        prediction.put("factors", List.of("Historical data", "Seasonal patterns", "Event schedules"));
        prediction.put("recommendation", "Consider donating to nearby NGOs");
        prediction.put("generatedAt", LocalDateTime.now());
        
        return prediction;
    }
    
    public void savePrediction(Prediction prediction) {
        predictionRepository.save(prediction);
    }
    
    public List<Prediction> getPredictionsByDonorId(Long donorId) {
        return predictionRepository.findByDonorId(donorId);
    }
    
    public List<Prediction> getActivePredictions() {
        return predictionRepository.findByStatus("ACTIVE");
    }
    
    public void updatePredictionStatus(Long predictionId, String status) {
        Prediction prediction = predictionRepository.findById(predictionId)
                .orElseThrow(() -> new RuntimeException("Prediction not found"));
        
        try {
            Prediction.PredictionStatus predictionStatus = Prediction.PredictionStatus.valueOf(status.toUpperCase());
            prediction.setStatus(predictionStatus);
            predictionRepository.save(prediction);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid prediction status: " + status);
        }
    }
} 