package com.ignithon.repository;

import com.ignithon.entity.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
    
    Optional<Volunteer> findByEmail(String email);
    
    List<Volunteer> findByCity(String city);
    
    List<Volunteer> findByVolunteerType(Volunteer.VolunteerType volunteerType);
    
    List<Volunteer> findByIsAvailableTrue();
    
    List<Volunteer> findByCityAndIsAvailableTrue(String city);
    
    @Query(value = "SELECT v.*, " +
                   "ST_Distance(ST_MakePoint(v.longitude, v.latitude), ST_MakePoint(:longitude, :latitude)) as distance " +
                   "FROM volunteers v " +
                   "WHERE v.is_available = true " +
                   "ORDER BY distance " +
                   "LIMIT :limit", nativeQuery = true)
    List<Volunteer> findAvailableVolunteersNearby(@Param("latitude") Double latitude, 
                                                   @Param("longitude") Double longitude, 
                                                   @Param("limit") Integer limit);
    
    @Query(value = "SELECT v.*, " +
                   "ST_Distance(ST_MakePoint(v.longitude, v.latitude), ST_MakePoint(:longitude, :latitude)) as distance " +
                   "FROM volunteers v " +
                   "WHERE v.is_available = true " +
                   "AND ST_Distance(ST_MakePoint(v.longitude, v.latitude), ST_MakePoint(:longitude, :latitude)) <= :maxDistance " +
                   "ORDER BY distance", nativeQuery = true)
    List<Volunteer> findAvailableVolunteersWithinDistance(@Param("latitude") Double latitude, 
                                                          @Param("longitude") Double longitude, 
                                                          @Param("maxDistance") Double maxDistance);
    
    List<Volunteer> findByOrderByPointsDesc();
    
    List<Volunteer> findByOrderByTotalPickupsDesc();
    
    List<Volunteer> findByOrderByAverageRatingDesc();
} 