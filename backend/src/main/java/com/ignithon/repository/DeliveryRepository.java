package com.ignithon.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ignithon.entity.Delivery;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    
    Optional<Delivery> findByEmail(String email);
    
    List<Delivery> findByCity(String city);
    
    List<Delivery> findByIsAvailableTrue();
    
    @Query("SELECT d FROM Delivery d WHERE d.isAvailable = true AND " +
           "SQRT(POWER(d.latitude - :latitude, 2) + POWER(d.longitude - :longitude, 2)) <= :maxDistance")
    List<Delivery> findAvailableDeliveriesNearLocation(
        @Param("latitude") Double latitude, 
        @Param("longitude") Double longitude, 
        @Param("maxDistance") Double maxDistance
    );
    
    List<Delivery> findByVehicleType(Delivery.VehicleType vehicleType);
    
    @Query("SELECT d FROM Delivery d WHERE d.averageRating >= :minRating ORDER BY d.averageRating DESC")
    List<Delivery> findByMinimumRating(@Param("minRating") Double minRating);
}
