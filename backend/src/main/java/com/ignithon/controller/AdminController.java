package com.ignithon.controller;

import com.ignithon.entity.Donor;
import com.ignithon.entity.NGO;
import com.ignithon.repository.DonorRepository;
import com.ignithon.repository.NGORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private NGORepository ngoRepository;

    /**
     * Get admin dashboard statistics
     */
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getDashboardStats() {
        try {
            List<Donor> allDonors = donorRepository.findAll();
            List<NGO> allNGOs = ngoRepository.findAll();
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalDonors", allDonors.size());
            stats.put("totalNGOs", allNGOs.size());
            stats.put("activeDonors", allDonors.stream().filter(Donor::isActive).count());
            stats.put("acceptingNGOs", allNGOs.stream().filter(NGO::isAcceptsDonations).count());
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get dashboard stats: " + e.getMessage());
        }
    }

    /**
     * Get all donors for admin view
     */
    @GetMapping("/donors")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllDonors() {
        try {
            List<Donor> donors = donorRepository.findAll();
            return ResponseEntity.ok(donors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get donors: " + e.getMessage());
        }
    }

    /**
     * Get all NGOs for admin view
     */
    @GetMapping("/ngos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllNGOs() {
        try {
            List<NGO> ngos = ngoRepository.findAll();
            return ResponseEntity.ok(ngos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get NGOs: " + e.getMessage());
        }
    }
}
