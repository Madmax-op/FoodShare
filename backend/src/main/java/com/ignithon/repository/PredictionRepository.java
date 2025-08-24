package com.ignithon.repository;

import com.ignithon.entity.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Long> {
    
    List<Prediction> findByDonorId(Long donorId);
    
    List<Prediction> findByStatus(String status);
    
    List<Prediction> findByDonorIdAndStatus(Long donorId, String status);
    
    @Query(value = "SELECT p.* FROM predictions p " +
                   "WHERE p.status = :status " +
                   "AND p.valid_until > NOW() " +
                   "ORDER BY p.prediction_date DESC", nativeQuery = true)
    List<Prediction> findActivePredictions(@Param("status") String status);
    
    @Query(value = "SELECT p.* FROM predictions p " +
                   "WHERE p.donor_id = :donorId " +
                   "AND p.status = :status " +
                   "ORDER BY p.prediction_date DESC", nativeQuery = true)
    List<Prediction> findByDonorIdAndStatusOrderByDate(@Param("donorId") Long donorId, 
                                                       @Param("status") String status);
    
    @Query(value = "SELECT p.* FROM predictions p " +
                   "WHERE p.food_type = :foodType " +
                   "AND p.status = 'ACTIVE' " +
                   "ORDER BY p.confidence DESC", nativeQuery = true)
    List<Prediction> findByFoodTypeOrderByConfidence(@Param("foodType") String foodType);
} 