package com.ignithon.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ignithon.service.MapsService;

@RestController
@RequestMapping("/maps")
@CrossOrigin(origins = "*")
public class MapsController {

    @Autowired
    private MapsService mapsService;

    /**
     * Root endpoint for Maps API
     */
    @GetMapping("")
    public ResponseEntity<?> getMapsApiInfo() {
        return ResponseEntity.ok(Map.of(
            "message", "Maps API is running",
            "endpoints", List.of(
                "/api/maps/health - Health check",
                "/api/maps/geocode - Geocode address",
                "/api/maps/geocode/reverse - Reverse geocode coordinates",
                "/api/maps/places/nearby - Find nearby places",
                "/api/maps/route/fastest - Get fastest route",
                "/api/maps/route/optimized - Get optimized route",
                "/api/maps/distance-matrix - Get distance matrix",
                "/api/maps/volunteer/optimize-pickup - Optimize volunteer pickup",
                "/api/maps/ngo-to-donor - Get NGO to donor route",
                "/api/maps/donor/nearby-ngos - Find nearby NGOs for donor",
                "/api/maps/ngo/nearby-donors - Find nearby donors for NGO"
            ),
            "status", "active"
        ));
    }

    /**
     * Test endpoint for debugging
     */
    @GetMapping("/test")
    public ResponseEntity<?> testEndpoint() {
        return ResponseEntity.ok(Map.of(
            "message", "Test endpoint working",
            "timestamp", System.currentTimeMillis()
        ));
    }

    /**
     * Get optimized route between multiple waypoints
     */
    @PostMapping("/route/optimized")
    public ResponseEntity<?> getOptimizedRoute(@RequestBody List<Map<String, Object>> waypoints) {
        try {
            Map<String, Object> result = mapsService.getOptimizedRoute(waypoints);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to get optimized route: " + e.getMessage()));
        }
    }

    /**
     * Get fastest route between two points
     */
    @GetMapping("/route/fastest")
    public ResponseEntity<?> getFastestRoute(
            @RequestParam Double startLat,
            @RequestParam Double startLng,
            @RequestParam Double endLat,
            @RequestParam Double endLng,
            @RequestParam(defaultValue = "driving") String mode) {
        try {
            Map<String, Object> result = mapsService.getFastestRoute(startLat, startLng, endLat, endLng, mode);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to get fastest route: " + e.getMessage()));
        }
    }

    /**
     * Get nearby places
     */
    @GetMapping("/places/nearby")
    public ResponseEntity<?> getNearbyPlaces(
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam(defaultValue = "food") String type,
            @RequestParam(defaultValue = "5000") Integer radius) {
        try {
            Map<String, Object> result = mapsService.getNearbyPlaces(lat, lng, type, radius);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to get nearby places: " + e.getMessage()));
        }
    }

    /**
     * Geocode an address to coordinates
     */
    @GetMapping("/geocode")
    public ResponseEntity<?> geocodeAddress(@RequestParam String address) {
        try {
            Map<String, Object> result = mapsService.geocodeAddress(address);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to geocode address: " + e.getMessage()));
        }
    }

    /**
     * Reverse geocode coordinates to address
     */
    @GetMapping("/geocode/reverse")
    public ResponseEntity<?> reverseGeocode(
            @RequestParam Double lat,
            @RequestParam Double lng) {
        try {
            Map<String, Object> result = mapsService.reverseGeocode(lat, lng);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to reverse geocode: " + e.getMessage()));
        }
    }

    /**
     * Get distance matrix between multiple points
     */
    @PostMapping("/distance-matrix")
    public ResponseEntity<?> getDistanceMatrix(
            @RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> origins = (List<Map<String, Object>>) request.get("origins");
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> destinations = (List<Map<String, Object>>) request.get("destinations");
            String mode = (String) request.getOrDefault("mode", "driving");
            
            Map<String, Object> result = mapsService.getDistanceMatrix(origins, destinations, mode);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to get distance matrix: " + e.getMessage()));
        }
    }

    /**
     * Get route for volunteer pickup optimization
     */
    @PostMapping("/volunteer/optimize-pickup")
    public ResponseEntity<?> optimizeVolunteerPickup(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> pickupPoints = (List<Map<String, Object>>) request.get("pickupPoints");
            Map<String, Object> volunteerLocation = (Map<String, Object>) request.get("volunteerLocation");
            
            // Add volunteer location as starting point
            pickupPoints.add(0, volunteerLocation);
            
            Map<String, Object> result = mapsService.getOptimizedRoute(pickupPoints);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to optimize pickup route: " + e.getMessage()));
        }
    }

    /**
     * Get route for NGO to donor
     */
    @GetMapping("/ngo-to-donor")
    public ResponseEntity<?> getNGOToDonorRoute(
            @RequestParam Double ngoLat,
            @RequestParam Double ngoLng,
            @RequestParam Double donorLat,
            @RequestParam Double donorLng,
            @RequestParam(defaultValue = "driving") String mode) {
        try {
            Map<String, Object> result = mapsService.getFastestRoute(ngoLat, ngoLng, donorLat, donorLng, mode);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to get NGO to donor route: " + e.getMessage()));
        }
    }

    /**
     * Get nearby NGOs for a donor
     */
    @GetMapping("/donor/nearby-ngos")
    public ResponseEntity<?> getNearbyNGOsForDonor(
            @RequestParam Double donorLat,
            @RequestParam Double donorLng,
            @RequestParam(defaultValue = "10000") Integer radius) {
        try {
            Map<String, Object> result = mapsService.getNearbyPlaces(donorLat, donorLng, "food", radius);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to get nearby NGOs: " + e.getMessage()));
        }
    }

    /**
     * Get nearby donors for an NGO
     */
    @GetMapping("/ngo/nearby-donors")
    public ResponseEntity<?> getNearbyDonorsForNGO(
            @RequestParam Double ngoLat,
            @RequestParam Double ngoLng,
            @RequestParam(defaultValue = "10000") Integer radius) {
        try {
            Map<String, Object> result = mapsService.getNearbyPlaces(ngoLat, ngoLng, "restaurant", radius);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to get nearby donors: " + e.getMessage()));
        }
    }

    /**
     * Health check for Maps API
     */
    @GetMapping("/health")
    public ResponseEntity<?> mapsHealthCheck() {
        try {
            // Use simple health check instead of external API call
            Map<String, Object> result = mapsService.healthCheck();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "status", "Maps API is not working",
                "error", e.getMessage()
            ));
        }
    }
}
