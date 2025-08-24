package com.ignithon.repository;

import com.ignithon.entity.Donation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    
    Page<Donation> findByDonorId(Long donorId, Pageable pageable);
    
    Long countByDonorId(Long donorId);
    
    Long countByDonorIdAndStatus(Long donorId, String status);
    
    List<Donation> findByStatus(String status);
    
    List<Donation> findByNgoId(Long ngoId);
    
    List<Donation> findByDonorIdAndStatus(Long donorId, String status);
    
    @Query(value = "SELECT COUNT(DISTINCT d.ngo_id) FROM donations d WHERE d.donor_id = :donorId", nativeQuery = true)
    Long countUniqueNGOsByDonorId(@Param("donorId") Long donorId);
    
    @Query(value = "SELECT d.*, " +
                   "ST_Distance(ST_MakePoint(d.longitude, d.latitude), ST_MakePoint(:longitude, :latitude)) as distance " +
                   "FROM donations d " +
                   "WHERE d.status = 'PENDING' " +
                   "AND ST_Distance(ST_MakePoint(d.longitude, d.latitude), ST_MakePoint(:longitude, :latitude)) <= :maxDistance " +
                   "ORDER BY distance", nativeQuery = true)
    List<Donation> findNearbyDonations(@Param("latitude") Double latitude, 
                                       @Param("longitude") Double longitude, 
                                       @Param("maxDistance") Double maxDistance);
    
    @Query(value = "SELECT d.* FROM donations d " +
                   "WHERE d.status = :status " +
                   "ORDER BY d.created_at DESC", nativeQuery = true)
    List<Donation> findByStatusOrderByCreatedAtDesc(@Param("status") String status);
    
    @Query(value = "SELECT d.* FROM donations d " +
                   "WHERE d.donor_id = :donorId " +
                   "AND d.status = :status " +
                   "ORDER BY d.created_at DESC", nativeQuery = true)
    List<Donation> findByDonorIdAndStatusOrderByCreatedAtDesc(@Param("donorId") Long donorId, 
                                                              @Param("status") String status);
} 