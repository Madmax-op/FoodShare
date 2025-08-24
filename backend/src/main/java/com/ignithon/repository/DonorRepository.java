package com.ignithon.repository;

import com.ignithon.entity.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonorRepository extends JpaRepository<Donor, Long> {
    
    List<Donor> findByCity(String city);
    
    List<Donor> findByDonorType(String donorType);
    
    @Query("SELECT d FROM Donor d WHERE d.isActive = true")
    List<Donor> findActiveDonors();
    
    @Query("SELECT d FROM Donor d WHERE d.city = :city AND d.isActive = true")
    List<Donor> findActiveDonorsByCity(@Param("city") String city);
} 