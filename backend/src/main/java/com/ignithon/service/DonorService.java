package com.ignithon.service;

import com.ignithon.entity.Donor;
import com.ignithon.entity.User;
import com.ignithon.repository.DonorRepository;
import com.ignithon.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class DonorService {
    
    private static final Logger logger = LoggerFactory.getLogger(DonorService.class);
    
    @Autowired
    private DonorRepository donorRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public Donor findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        
        if (user instanceof Donor) {
            return (Donor) user;
        } else {
            throw new RuntimeException("User with email " + email + " is not a donor");
        }
    }
    
    public Donor findById(Long id) {
        return donorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donor not found with id: " + id));
    }
    
    public Donor updateProfile(Long donorId, Donor donorUpdate) {
        Donor existingDonor = findById(donorId);
        
        // Update only allowed fields
        if (donorUpdate.getName() != null) {
            existingDonor.setName(donorUpdate.getName());
        }
        if (donorUpdate.getPhone() != null) {
            existingDonor.setPhone(donorUpdate.getPhone());
        }
        if (donorUpdate.getCity() != null) {
            existingDonor.setCity(donorUpdate.getCity());
        }
        if (donorUpdate.getLatitude() != null) {
            existingDonor.setLatitude(donorUpdate.getLatitude());
        }
        if (donorUpdate.getLongitude() != null) {
            existingDonor.setLongitude(donorUpdate.getLongitude());
        }
        if (donorUpdate.getDescription() != null) {
            existingDonor.setDescription(donorUpdate.getDescription());
        }
        
        return donorRepository.save(existingDonor);
    }
    
    public List<Donor> findByCity(String city) {
        return donorRepository.findByCity(city);
    }
    
    public List<Donor> findByDonorType(String donorType) {
        return donorRepository.findByDonorType(donorType);
    }
    
    public List<Donor> findActiveDonors() {
        return donorRepository.findActiveDonors();
    }
    
    public List<Donor> findActiveDonorsByCity(String city) {
        return donorRepository.findActiveDonorsByCity(city);
    }
    
    public Map<String, Object> getDonorBadges(Long donorId) {
        // This would typically come from a separate badge system
        // For now, returning mock data
        Map<String, Object> badges = new HashMap<>();
        badges.put("firstDonation", true);
        badges.put("regularDonor", true);
        badges.put("environmentalHero", true);
        badges.put("communityChampion", true);
        return badges;
    }
    
    public Integer getDonorRank(Long donorId) {
        // This would calculate donor's rank based on total donations/impact
        // For now, returning mock data
        return 15; // Top 15 donor
    }
    
    public Map<String, Object> getDonorStats(Long donorId) {
        Donor donor = findById(donorId);
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalDonations", donor.getTotalDonations());
        stats.put("averageDonationQuantity", donor.getAverageDonationQuantity());
        stats.put("isActive", donor.isActive());
        return stats;
    }
} 