package com.ignithon.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ignithon.entity.Donation;
import com.ignithon.entity.Donor;
import com.ignithon.service.DonationService;
import com.ignithon.service.DonorService;
import com.ignithon.service.PredictionService;

@RestController
@RequestMapping("/donor")
@CrossOrigin(origins = "*")
public class DonorDashboardController {

    @Autowired
    private DonorService donorService;

    @Autowired
    private DonationService donationService;

    @Autowired
    private PredictionService predictionService;

    /**
     * Get donor's dashboard overview
     */
    @GetMapping("/overview")
    public ResponseEntity<?> getDashboardOverview() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Donor donor = donorService.findByEmail(email);
            
            Map<String, Object> overview = new HashMap<>();
            overview.put("donor", donor);
            overview.put("totalDonations", donationService.countByDonorId(donor.getId()));
            overview.put("pendingDonations", donationService.countByDonorIdAndStatus(donor.getId(), "PENDING"));
            overview.put("completedDonations", donationService.countByDonorIdAndStatus(donor.getId(), "DELIVERED"));
            overview.put("totalMealsDonated", donationService.calculateTotalMealsDonated(donor.getId()));
            overview.put("co2Saved", donationService.calculateCO2Saved(donor.getId()));
            overview.put("badges", donorService.getDonorBadges(donor.getId()));
            
            return ResponseEntity.ok(overview);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to get dashboard overview: " + e.getMessage()));
        }
    }

    /**
     * Get donor's donation history
     */
    @GetMapping("/donations")
    public ResponseEntity<?> getDonationHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Donor donor = donorService.findByEmail(email);
            
            List<Donation> donations = donationService.findByDonorId(donor.getId(), page, size);
            return ResponseEntity.ok(donations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to get donation history: " + e.getMessage()));
        }
    }

    /**
     * Get donor's impact metrics
     */
    @GetMapping("/impact")
    public ResponseEntity<?> getImpactMetrics() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Donor donor = donorService.findByEmail(email);
            
            Map<String, Object> impact = new HashMap<>();
            impact.put("totalMealsDonated", donationService.calculateTotalMealsDonated(donor.getId()));
            impact.put("co2Saved", donationService.calculateCO2Saved(donor.getId()));
            impact.put("totalDonations", donationService.countByDonorId(donor.getId()));
            impact.put("ngosHelped", donationService.countUniqueNGOsHelped(donor.getId()));
            impact.put("badges", donorService.getDonorBadges(donor.getId()));
            impact.put("rank", donorService.getDonorRank(donor.getId()));
            
            return ResponseEntity.ok(impact);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to get impact metrics: " + e.getMessage()));
        }
    }

    /**
     * Get donor's predictions
     */
    @GetMapping("/predictions")
    public ResponseEntity<?> getPredictions() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Donor donor = donorService.findByEmail(email);
            
            List<Map<String, Object>> predictions = predictionService.getDonorPredictions(donor.getId());
            return ResponseEntity.ok(predictions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to get predictions: " + e.getMessage()));
        }
    }

    /**
     * Get donor's reports (CSR, Tax)
     */
    @GetMapping("/reports")
    public ResponseEntity<?> getReports(
            @RequestParam String reportType,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Donor donor = donorService.findByEmail(email);
            
            Map<String, Object> report = donationService.generateDonorReport(donor.getId(), reportType, startDate, endDate);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to generate report: " + e.getMessage()));
        }
    }

    /**
     * Duplicate a previous donation
     */
    @PostMapping("/donations/{donationId}/duplicate")
    public ResponseEntity<?> duplicateDonation(@PathVariable Long donationId) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Donor donor = donorService.findByEmail(email);
            
            Donation duplicatedDonation = donationService.duplicateDonation(donationId, donor.getId());
            return ResponseEntity.ok(duplicatedDonation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to duplicate donation: " + e.getMessage()));
        }
    }

    /**
     * Cancel a pending donation
     */
    @PostMapping("/donations/{donationId}/cancel")
    public ResponseEntity<?> cancelDonation(@PathVariable Long donationId) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Donor donor = donorService.findByEmail(email);
            
            donationService.cancelDonation(donationId, donor.getId());
            return ResponseEntity.ok(Map.of("message", "Donation cancelled successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to cancel donation: " + e.getMessage()));
        }
    }

    /**
     * Get donor's profile
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Donor donor = donorService.findByEmail(email);
            
            return ResponseEntity.ok(donor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to get profile: " + e.getMessage()));
        }
    }

    /**
     * Update donor's profile
     */
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody Donor donorUpdate) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Donor donor = donorService.findByEmail(email);
            
            Donor updatedDonor = donorService.updateProfile(donor.getId(), donorUpdate);
            return ResponseEntity.ok(updatedDonor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to update profile: " + e.getMessage()));
        }
    }
} 