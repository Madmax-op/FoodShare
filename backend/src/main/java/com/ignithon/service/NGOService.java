package com.ignithon.service;

import com.ignithon.dto.NearbyNgoResponse;
import com.ignithon.entity.NGO;
import com.ignithon.repository.NGORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NGOService {

    @Autowired
    private NGORepository ngoRepository;

    /**
     * Find nearby NGOs within a specified radius (in kilometers)
     */
    public List<NearbyNgoResponse> findNearbyNGOs(Double donorLatitude, Double donorLongitude, Double radiusKm) {
        List<NGO> allNGOs = ngoRepository.findByAcceptsDonationsTrue();
        
        return allNGOs.stream()
                .map(ngo -> {
                    double distance = calculateDistance(donorLatitude, donorLongitude, 
                                                      ngo.getLatitude(), ngo.getLongitude());
                    return new NearbyNgoResponse(
                        ngo.getId(),
                        ngo.getName(),
                        ngo.getOrganizationType(),
                        ngo.getDescription(),
                        ngo.getCity(),
                        ngo.getLatitude(),
                        ngo.getLongitude(),
                        distance,
                        ngo.isAcceptsDonations(),
                        ngo.getMaxDonationDistance(),
                        ngo.getMaxDonationQuantity()
                    );
                })
                .filter(ngo -> ngo.getDistance() <= radiusKm)
                .sorted((a, b) -> Double.compare(a.getDistance(), b.getDistance()))
                .collect(Collectors.toList());
    }

    /**
     * Calculate distance between two points using Haversine formula
     * Returns distance in kilometers
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in kilometers

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // convert to kilometers

        return distance;
    }

    /**
     * Get all NGOs for admin dashboard
     */
    public List<NGO> getAllNGOs() {
        return ngoRepository.findAll();
    }

    /**
     * Get NGO by ID
     */
    public NGO getNGOById(Long id) {
        return ngoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("NGO not found with id: " + id));
    }
}
