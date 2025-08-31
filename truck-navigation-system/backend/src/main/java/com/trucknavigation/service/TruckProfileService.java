package com.trucknavigation.service;

import com.trucknavigation.dto.TruckProfileDto;
import com.trucknavigation.model.TruckProfile;
import com.trucknavigation.model.User;
import com.trucknavigation.repository.TruckProfileRepository;
import com.trucknavigation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TruckProfileService {
    
    @Autowired
    private TruckProfileRepository truckProfileRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Get all active truck profiles for the current user
     */
    public List<TruckProfileDto> getUserTruckProfiles() {
        Long userId = getCurrentUserId();
        return truckProfileRepository.findByUserIdAndIsActiveTrue(userId)
                .stream()
                .map(TruckProfileDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * Get a specific truck profile by ID for the current user
     */
    public Optional<TruckProfileDto> getTruckProfile(Long id) {
        Long userId = getCurrentUserId();
        return truckProfileRepository.findByIdAndUserId(id, userId)
                .filter(TruckProfile::isActive)
                .map(TruckProfileDto::fromEntity);
    }
    
    /**
     * Create a new truck profile for the current user
     */
    public TruckProfileDto createTruckProfile(TruckProfileDto truckProfileDto) {
        Long userId = getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Check if profile name already exists for this user
        if (truckProfileRepository.existsByNameAndUserIdAndIsActiveTrue(truckProfileDto.getName(), userId)) {
            throw new RuntimeException("Truck profile with this name already exists");
        }
        
        TruckProfile truckProfile = truckProfileDto.toEntity();
        truckProfile.setUser(user);
        truckProfile.setActive(true);
        
        TruckProfile savedProfile = truckProfileRepository.save(truckProfile);
        return TruckProfileDto.fromEntity(savedProfile);
    }
    
    /**
     * Update an existing truck profile
     */
    public TruckProfileDto updateTruckProfile(Long id, TruckProfileDto truckProfileDto) {
        Long userId = getCurrentUserId();
        TruckProfile existingProfile = truckProfileRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Truck profile not found"));
        
        // Check if new name conflicts with existing profiles (excluding current one)
        if (!existingProfile.getName().equals(truckProfileDto.getName()) &&
            truckProfileRepository.existsByNameAndUserIdAndIsActiveTrue(truckProfileDto.getName(), userId)) {
            throw new RuntimeException("Truck profile with this name already exists");
        }
        
        // Update fields
        existingProfile.setName(truckProfileDto.getName());
        existingProfile.setDescription(truckProfileDto.getDescription());
        existingProfile.setHeight(truckProfileDto.getHeight());
        existingProfile.setWidth(truckProfileDto.getWidth());
        existingProfile.setLength(truckProfileDto.getLength());
        existingProfile.setMaxWeight(truckProfileDto.getMaxWeight());
        existingProfile.setMaxAxleLoad(truckProfileDto.getMaxAxleLoad());
        existingProfile.setNumberOfAxles(truckProfileDto.getNumberOfAxles());
        existingProfile.setTruckType(truckProfileDto.getTruckType());
        existingProfile.setCargoType(truckProfileDto.getCargoType());
        existingProfile.setEmissionStandard(truckProfileDto.getEmissionStandard());
        existingProfile.setRegistrationNumber(truckProfileDto.getRegistrationNumber());
        existingProfile.setHasNationalPermit(truckProfileDto.isHasNationalPermit());
        existingProfile.setHasOversizePermit(truckProfileDto.isHasOversizePermit());
        existingProfile.setHasHazmatPermit(truckProfileDto.isHasHazmatPermit());
        
        TruckProfile updatedProfile = truckProfileRepository.save(existingProfile);
        return TruckProfileDto.fromEntity(updatedProfile);
    }
    
    /**
     * Soft delete a truck profile (mark as inactive)
     */
    public void deleteTruckProfile(Long id) {
        Long userId = getCurrentUserId();
        TruckProfile truckProfile = truckProfileRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Truck profile not found"));
        
        truckProfile.setActive(false);
        truckProfileRepository.save(truckProfile);
    }
    
    /**
     * Get truck profiles by type
     */
    public List<TruckProfileDto> getTruckProfilesByType(TruckProfile.TruckType truckType) {
        return truckProfileRepository.findByTruckTypeAndIsActiveTrue(truckType)
                .stream()
                .map(TruckProfileDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * Validate truck profile dimensions and specifications
     */
    public boolean validateTruckProfile(TruckProfileDto truckProfileDto) {
        // Basic validation - ensure dimensions are reasonable for Indian roads
        if (truckProfileDto.getHeight().doubleValue() > 4.5) { // Max height for most Indian bridges
            throw new RuntimeException("Truck height exceeds maximum limit of 4.5m for Indian roads");
        }
        
        if (truckProfileDto.getWidth().doubleValue() > 2.5) { // Standard road width limit
            throw new RuntimeException("Truck width exceeds maximum limit of 2.5m");
        }
        
        if (truckProfileDto.getLength().doubleValue() > 18.75) { // Legal limit in India
            throw new RuntimeException("Truck length exceeds maximum limit of 18.75m");
        }
        
        if (truckProfileDto.getMaxWeight().doubleValue() > 55.0) { // GVW limit in India
            throw new RuntimeException("Truck weight exceeds maximum limit of 55 tonnes");
        }
        
        return true;
    }
    
    /**
     * Get the current authenticated user's ID
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
    
    /**
     * Check if user can create more truck profiles (business rule: max 10 profiles per user)
     */
    public boolean canCreateMoreProfiles() {
        Long userId = getCurrentUserId();
        long profileCount = truckProfileRepository.countActiveProfilesByUser(userId);
        return profileCount < 10; // Business rule: max 10 profiles per user
    }
    
    /**
     * Get count of active profiles for current user
     */
    public long getActiveProfileCount() {
        Long userId = getCurrentUserId();
        return truckProfileRepository.countActiveProfilesByUser(userId);
    }
}
