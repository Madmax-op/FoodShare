package com.ignithon.repository;

import com.ignithon.entity.NGO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NGORepository extends JpaRepository<NGO, Long> {
    
    List<NGO> findByCity(String city);
    
    List<NGO> findByAcceptsDonationsTrue();
    
    @Query(value = "SELECT *, " +
                   "ST_Distance(ST_MakePoint(longitude, latitude), ST_MakePoint(:longitude, :latitude)) as distance " +
                   "FROM ngos " +
                   "WHERE accepts_donations = true " +
                   "ORDER BY distance " +
                   "LIMIT :limit", nativeQuery = true)
    List<NGO> findNearbyNGOs(@Param("latitude") Double latitude, 
                              @Param("longitude") Double longitude, 
                              @Param("limit") int limit);
    
    @Query(value = "SELECT *, " +
                   "ST_Distance(ST_MakePoint(longitude, latitude), ST_MakePoint(:longitude, :latitude)) as distance " +
                   "FROM ngos " +
                   "WHERE accepts_donations = true " +
                   "AND ST_Distance(ST_MakePoint(longitude, latitude), ST_MakePoint(:longitude, :latitude)) <= :maxDistance " +
                   "ORDER BY distance", nativeQuery = true)
    List<NGO> findNGOsWithinDistance(@Param("latitude") Double latitude, 
                                     @Param("longitude") Double longitude, 
                                     @Param("maxDistance") Double maxDistance);
} 