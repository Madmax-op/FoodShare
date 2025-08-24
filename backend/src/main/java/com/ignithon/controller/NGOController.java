package com.ignithon.controller;

import com.ignithon.dto.NearbyNgoResponse;
import com.ignithon.entity.NGO;
import com.ignithon.service.NGOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ngo")
@CrossOrigin(origins = "*")
public class NGOController {

    @Autowired
    private NGOService ngoService;

    /**
     * Find nearby NGOs for donors
     */
    @GetMapping("/nearby")
    @PreAuthorize("hasRole('DONOR')")
    public ResponseEntity<?> findNearbyNGOs(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "50.0") Double radiusKm) {
        try {
            List<NearbyNgoResponse> nearbyNGOs = ngoService.findNearbyNGOs(latitude, longitude, radiusKm);
            return ResponseEntity.ok(nearbyNGOs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to find nearby NGOs: " + e.getMessage());
        }
    }

    /**
     * Get all NGOs for admin dashboard
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllNGOs() {
        try {
            List<NGO> ngos = ngoService.getAllNGOs();
            return ResponseEntity.ok(ngos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get NGOs: " + e.getMessage());
        }
    }

    /**
     * Get NGO by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getNGOById(@PathVariable Long id) {
        try {
            NGO ngo = ngoService.getNGOById(id);
            return ResponseEntity.ok(ngo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get NGO: " + e.getMessage());
        }
    }
}
