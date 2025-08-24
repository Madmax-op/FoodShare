package com.ignithon.service;

import com.ignithon.dto.NGORegistrationRequest;
import com.ignithon.dto.DonorRegistrationRequest;
import com.ignithon.dto.VolunteerRegistrationRequest;
import com.ignithon.dto.DeliveryRegistrationRequest;
import com.ignithon.dto.SimpleRegistrationRequest;
import com.ignithon.entity.NGO;
import com.ignithon.entity.Donor;
import com.ignithon.entity.Volunteer;
import com.ignithon.entity.Delivery;
import com.ignithon.entity.Admin;
import com.ignithon.entity.User;
import com.ignithon.repository.NGORepository;
import com.ignithon.repository.DonorRepository;
import com.ignithon.repository.VolunteerRepository;
import com.ignithon.repository.DeliveryRepository;
import com.ignithon.repository.AdminRepository;
import com.ignithon.repository.UserRepository;
import com.ignithon.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NGORepository ngoRepository;

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public NGO registerNGO(NGORegistrationRequest request) {
        // Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // Create new NGO
        NGO ngo = new NGO();
        ngo.setName(request.getName());
        ngo.setEmail(request.getEmail());
        ngo.setPhone(request.getPhone());
        ngo.setCity(request.getCity());
        ngo.setLatitude(request.getLatitude());
        ngo.setLongitude(request.getLongitude());
        ngo.setPassword(passwordEncoder.encode(request.getPassword()));
        ngo.setOrganizationType(request.getOrganizationType());
        ngo.setDescription(request.getDescription());

        NGO savedNGO = ngoRepository.save(ngo);
        
        // Send welcome email
        try {
            emailService.sendWelcomeEmail(savedNGO);
            logger.info("Welcome email sent to NGO: {}", savedNGO.getEmail());
        } catch (Exception e) {
            logger.warn("Failed to send welcome email to NGO: {}", savedNGO.getEmail(), e);
        }
        
        return savedNGO;
    }

    public Donor registerDonor(DonorRegistrationRequest request) {
        // Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // Create new Donor
        Donor donor = new Donor();
        donor.setName(request.getName());
        donor.setEmail(request.getEmail());
        donor.setPhone(request.getPhone());
        donor.setCity(request.getCity());
        donor.setLatitude(request.getLatitude());
        donor.setLongitude(request.getLongitude());
        donor.setPassword(passwordEncoder.encode(request.getPassword()));
        donor.setDonorType(request.getDonorType());
        donor.setDescription(request.getDescription());

        Donor savedDonor = donorRepository.save(donor);
        
        // Send welcome email
        try {
            emailService.sendWelcomeEmail(savedDonor);
            logger.info("Welcome email sent to Donor: {}", savedDonor.getEmail());
        } catch (Exception e) {
            logger.warn("Failed to send welcome email to Donor: {}", savedDonor.getEmail(), e);
        }
        
        return savedDonor;
    }

    public Volunteer registerVolunteer(VolunteerRegistrationRequest request) {
        // Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // Create new Volunteer
        Volunteer volunteer = new Volunteer();
        volunteer.setName(request.getName());
        volunteer.setEmail(request.getEmail());
        volunteer.setPhone(request.getPhone());
        volunteer.setCity(request.getCity());
        volunteer.setLatitude(request.getLatitude());
        volunteer.setLongitude(request.getLongitude());
        volunteer.setPassword(passwordEncoder.encode(request.getPassword()));
        volunteer.setVolunteerType(request.getVolunteerType());
        volunteer.setSkills(request.getSkills());
        volunteer.setAvailabilitySchedule(request.getAvailabilitySchedule());
        volunteer.setMaxPickupDistance(request.getMaxPickupDistance());

        Volunteer savedVolunteer = volunteerRepository.save(volunteer);
        
        // Send welcome email
        try {
            emailService.sendWelcomeEmail(savedVolunteer);
            logger.info("Welcome email sent to Volunteer: {}", savedVolunteer.getEmail());
        } catch (Exception e) {
            logger.warn("Failed to send welcome email to Volunteer: {}", savedVolunteer.getEmail(), e);
        }
        
        return savedVolunteer;
    }

    public Delivery registerDelivery(DeliveryRegistrationRequest request) {
        // Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // Create new Delivery
        Delivery delivery = new Delivery();
        delivery.setName(request.getName());
        delivery.setEmail(request.getEmail());
        delivery.setPhone(request.getPhone());
        delivery.setCity(request.getCity());
        delivery.setLatitude(request.getLatitude());
        delivery.setLongitude(request.getLongitude());
        delivery.setPassword(passwordEncoder.encode(request.getPassword()));
        delivery.setVehicleType(request.getVehicleType());
        delivery.setLicenseNumber(request.getLicenseNumber());
        delivery.setVehicleModel(request.getVehicleModel());
        delivery.setVehicleNumber(request.getVehicleNumber());
        delivery.setMaxDeliveryDistance(request.getMaxDeliveryDistance());
        delivery.setDeliveryAreas(request.getDeliveryAreas());

        Delivery savedDelivery = deliveryRepository.save(delivery);
        
        // Send welcome email
        try {
            emailService.sendWelcomeEmail(savedDelivery);
            logger.info("Welcome email sent to Delivery: {}", savedDelivery.getEmail());
        } catch (Exception e) {
            logger.warn("Failed to send welcome email to Delivery: {}", savedDelivery.getEmail(), e);
        }
        
        return savedDelivery;
    }

    public String generateToken(Authentication authentication) {
        return jwtTokenProvider.generateToken(authentication);
    }

    public String generateToken(String username) {
        return jwtTokenProvider.generateToken(username);
    }

    public User registerUser(SimpleRegistrationRequest request) {
        // Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User savedUser = null;

        switch (request.getRole()) {
            case NGO:
                NGO ngo = new NGO();
                ngo.setName(request.getName());
                ngo.setEmail(request.getEmail());
                ngo.setPhone(request.getPhone());
                ngo.setCity(request.getCity());
                ngo.setLatitude(request.getLatitude());
                ngo.setLongitude(request.getLongitude());
                ngo.setPassword(passwordEncoder.encode(request.getPassword()));
                ngo.setOrganizationType(request.getOrganizationType());
                ngo.setDescription(request.getNgoDescription());
                
                savedUser = ngoRepository.save(ngo);
                break;
                
            case DONOR:
                Donor donor = new Donor();
                donor.setName(request.getName());
                donor.setEmail(request.getEmail());
                donor.setPhone(request.getPhone());
                donor.setCity(request.getCity());
                donor.setLatitude(request.getLatitude());
                donor.setLongitude(request.getLongitude());
                donor.setPassword(passwordEncoder.encode(request.getPassword()));
                donor.setDonorType(request.getDonorType());
                donor.setDescription(request.getDonorDescription());
                
                savedUser = donorRepository.save(donor);
                break;
                
            case ADMIN:
                Admin admin = new Admin();
                admin.setName(request.getName());
                admin.setEmail(request.getEmail());
                admin.setPhone(request.getPhone());
                admin.setCity(request.getCity());
                admin.setLatitude(request.getLatitude());
                admin.setLongitude(request.getLongitude());
                admin.setPassword(passwordEncoder.encode(request.getPassword()));
                admin.setSuperAdmin(request.getIsSuperAdmin() != null ? request.getIsSuperAdmin() : false);
                admin.setAdminLevel(request.getAdminLevel() != null ? request.getAdminLevel() : "ADMIN");
                
                savedUser = adminRepository.save(admin);
                break;
                
            default:
                throw new RuntimeException("Invalid role specified");
        }
        
        // Send welcome email
        try {
            emailService.sendWelcomeEmail(savedUser);
            logger.info("Welcome email sent to {}: {}", savedUser.getRole(), savedUser.getEmail());
        } catch (Exception e) {
            logger.warn("Failed to send welcome email to {}: {}", savedUser.getRole(), savedUser.getEmail(), e);
        }
        
        return savedUser;
    }
} 