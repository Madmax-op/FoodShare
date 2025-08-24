package com.ignithon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ignithon.dto.AuthRequest;
import com.ignithon.dto.AuthResponse;
import com.ignithon.dto.DeliveryRegistrationRequest;
import com.ignithon.dto.DonorRegistrationRequest;
import com.ignithon.dto.NGORegistrationRequest;
import com.ignithon.dto.SimpleRegistrationRequest;
import com.ignithon.dto.UnifiedRegistrationRequest;
import com.ignithon.dto.VolunteerRegistrationRequest;
import com.ignithon.entity.Delivery;
import com.ignithon.entity.Donor;
import com.ignithon.entity.NGO;
import com.ignithon.entity.User;
import com.ignithon.entity.Volunteer;
import com.ignithon.service.AuthService;
import com.ignithon.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register/ngo")
    public ResponseEntity<?> registerNGO(@Valid @RequestBody NGORegistrationRequest request) {
        try {
            NGO ngo = authService.registerNGO(request);
            return ResponseEntity.ok(new AuthResponse("NGO registered successfully", ngo.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthResponse("Registration failed: " + e.getMessage(), null));
        }
    }

    @PostMapping("/register/donor")
    public ResponseEntity<?> registerDonor(@Valid @RequestBody DonorRegistrationRequest request) {
        try {
            Donor donor = authService.registerDonor(request);
            return ResponseEntity.ok(new AuthResponse("Donor registered successfully", donor.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthResponse("Registration failed: " + e.getMessage(), null));
        }
    }

    @PostMapping("/register/volunteer")
    public ResponseEntity<?> registerVolunteer(@Valid @RequestBody VolunteerRegistrationRequest request) {
        try {
            Volunteer volunteer = authService.registerVolunteer(request);
            return ResponseEntity.ok(new AuthResponse("Volunteer registered successfully", volunteer.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthResponse("Registration failed: " + e.getMessage(), null));
        }
    }

    @PostMapping("/register/delivery")
    public ResponseEntity<?> registerDelivery(@Valid @RequestBody DeliveryRegistrationRequest request) {
        try {
            Delivery delivery = authService.registerDelivery(request);
            return ResponseEntity.ok(new AuthResponse("Delivery personnel registered successfully", delivery.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthResponse("Registration failed: " + e.getMessage(), null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UnifiedRegistrationRequest request) {
        try {
            switch (request.getRole()) {
                case NGO:
                    NGORegistrationRequest ngoRequest = new NGORegistrationRequest();
                    ngoRequest.setName(request.getName());
                    ngoRequest.setEmail(request.getEmail());
                    ngoRequest.setPhone(request.getPhone());
                    ngoRequest.setCity(request.getCity());
                    ngoRequest.setLatitude(request.getLatitude());
                    ngoRequest.setLongitude(request.getLongitude());
                    ngoRequest.setPassword(request.getPassword());
                    ngoRequest.setOrganizationType(request.getNgoType());
                    ngoRequest.setDescription(request.getNgoDescription());
                    
                    NGO ngo = authService.registerNGO(ngoRequest);
                    return ResponseEntity.ok(new AuthResponse("NGO registered successfully", ngo.getId()));
                    
                case DONOR:
                    DonorRegistrationRequest donorRequest = new DonorRegistrationRequest();
                    donorRequest.setName(request.getName());
                    donorRequest.setEmail(request.getEmail());
                    donorRequest.setPhone(request.getPhone());
                    donorRequest.setCity(request.getCity());
                    donorRequest.setLatitude(request.getLatitude());
                    donorRequest.setLongitude(request.getLongitude());
                    donorRequest.setPassword(request.getPassword());
                    donorRequest.setDonorType(request.getDonorType());
                    donorRequest.setDescription(request.getDonorDescription());
                    
                    Donor donor = authService.registerDonor(donorRequest);
                    return ResponseEntity.ok(new AuthResponse("Donor registered successfully", donor.getId()));
                    
                case ADMIN:
                    // For admin registration, we'll use the simple registration endpoint
                    return ResponseEntity.badRequest().body(new AuthResponse("Please use /auth/register/simple for admin registration", null));
                    
                default:
                    return ResponseEntity.badRequest().body(new AuthResponse("Invalid role specified", null));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthResponse("Registration failed: " + e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = authService.generateToken(authentication);
            
            User user = userService.findByEmail(request.getEmail());
            return ResponseEntity.ok(new AuthResponse("Login successful", user.getId(), jwt, user.getRole().toString()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthResponse("Login failed: " + e.getMessage(), null));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findByEmail(email);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthResponse("Failed to get user info: " + e.getMessage(), null));
        }
    }

    @PostMapping("/register/simple")
    public ResponseEntity<?> registerSimple(@Valid @RequestBody SimpleRegistrationRequest request) {
        try {
            User user = authService.registerUser(request);
            return ResponseEntity.ok(new AuthResponse(user.getRole() + " registered successfully", user.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthResponse("Registration failed: " + e.getMessage(), null));
        }
    }
} 