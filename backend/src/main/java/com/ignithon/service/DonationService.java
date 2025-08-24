package com.ignithon.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ignithon.entity.Donation;
import com.ignithon.repository.DonationRepository;
import com.ignithon.repository.DonorRepository;
import com.ignithon.repository.NGORepository;

@Service
public class DonationService {
    
    private static final Logger logger = LoggerFactory.getLogger(DonationService.class);
    
    @Autowired
    private DonationRepository donationRepository;
    
    @Autowired
    private DonorRepository donorRepository;
    
    @Autowired
    private NGORepository ngoRepository;
    
    public List<Donation> findByDonorId(Long donorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Donation> donationPage = donationRepository.findByDonorId(donorId, pageable);
        return donationPage.getContent();
    }
    
    public Long countByDonorId(Long donorId) {
        return donationRepository.countByDonorId(donorId);
    }
    
    public Long countByDonorIdAndStatus(Long donorId, String status) {
        return donationRepository.countByDonorIdAndStatus(donorId, status);
    }
    
    public Integer calculateTotalMealsDonated(Long donorId) {
        // This would calculate based on food type and quantity
        // For now, returning mock data
        return 150; // 150 meals donated
    }
    
    public Double calculateCO2Saved(Long donorId) {
        // This would calculate CO2 saved based on food waste prevention
        // For now, returning mock data
        return 25.5; // 25.5 kg CO2 saved
    }
    
    public Long countUniqueNGOsHelped(Long donorId) {
        return donationRepository.countUniqueNGOsByDonorId(donorId);
    }
    
    public Donation duplicateDonation(Long donationId, Long donorId) {
        Donation originalDonation = donationRepository.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Donation not found"));
        
        if (!originalDonation.getDonor().getId().equals(donorId)) {
            throw new RuntimeException("Unauthorized to duplicate this donation");
        }
        
        Donation duplicatedDonation = new Donation();
        duplicatedDonation.setDonor(originalDonation.getDonor());
        duplicatedDonation.setNgo(originalDonation.getNgo());
        duplicatedDonation.setFoodDetails(originalDonation.getFoodDetails());
        duplicatedDonation.setQuantity(originalDonation.getQuantity());
        duplicatedDonation.setFoodType(originalDonation.getFoodType());
        duplicatedDonation.setSpecialInstructions(originalDonation.getSpecialInstructions());
        duplicatedDonation.setStatus(Donation.DonationStatus.PENDING);
        
        return donationRepository.save(duplicatedDonation);
    }
    
    public void cancelDonation(Long donationId, Long donorId) {
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Donation not found"));
        
        if (!donation.getDonor().getId().equals(donorId)) {
            throw new RuntimeException("Unauthorized to cancel this donation");
        }
        
        if (donation.getStatus() != Donation.DonationStatus.PENDING) {
            throw new RuntimeException("Only pending donations can be cancelled");
        }
        
        donation.setStatus(Donation.DonationStatus.REJECTED);
        donationRepository.save(donation);
    }
    
    public Map<String, Object> generateDonorReport(Long donorId, String reportType, String startDate, String endDate) {
        // This would generate detailed reports based on report type and date range
        // For now, returning mock data
        Map<String, Object> report = new HashMap<>();
        report.put("donorId", donorId);
        report.put("reportType", reportType);
        report.put("startDate", startDate);
        report.put("endDate", endDate);
        report.put("totalDonations", 25);
        report.put("totalMealsDonated", 150);
        report.put("co2Saved", 25.5);
        report.put("ngosHelped", 8);
        report.put("generatedAt", LocalDateTime.now());
        
        return report;
    }
    
    public List<Donation> findNearbyDonations(Double latitude, Double longitude, Double maxDistance) {
        return donationRepository.findNearbyDonations(latitude, longitude, maxDistance);
    }
    
    public List<Donation> findDonationsByStatus(String status) {
        return donationRepository.findByStatus(status);
    }
    
    public Donation updateDonationStatus(Long donationId, String newStatus) {
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Donation not found"));
        
        try {
            Donation.DonationStatus status = Donation.DonationStatus.valueOf(newStatus.toUpperCase());
            donation.setStatus(status);
            return donationRepository.save(donation);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid donation status: " + newStatus);
        }
    }
} 