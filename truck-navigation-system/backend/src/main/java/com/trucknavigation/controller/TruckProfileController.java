package com.trucknavigation.controller;

import com.trucknavigation.dto.TruckProfileDto;
import com.trucknavigation.model.TruckProfile;
import com.trucknavigation.service.TruckProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/truck-profiles")
@Tag(name = "Truck Profile Management", description = "APIs for managing truck profiles and specifications")
public class TruckProfileController {
    
    @Autowired
    private TruckProfileService truckProfileService;
    
    @GetMapping
    @Operation(summary = "Get all truck profiles for current user", 
               description = "Retrieves all active truck profiles belonging to the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved truck profiles"),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    public ResponseEntity<List<TruckProfileDto>> getUserTruckProfiles() {
        try {
            List<TruckProfileDto> profiles = truckProfileService.getUserTruckProfiles();
            return ResponseEntity.ok(profiles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get truck profile by ID", 
               description = "Retrieves a specific truck profile by its ID for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved truck profile"),
        @ApiResponse(responseCode = "404", description = "Truck profile not found"),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    public ResponseEntity<TruckProfileDto> getTruckProfile(
            @Parameter(description = "Truck profile ID") @PathVariable Long id) {
        try {
            return truckProfileService.getTruckProfile(id)
                    .map(profile -> ResponseEntity.ok(profile))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping
    @Operation(summary = "Create new truck profile", 
               description = "Creates a new truck profile for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Truck profile created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "409", description = "Truck profile name already exists")
    })
    public ResponseEntity<?> createTruckProfile(@Valid @RequestBody TruckProfileDto truckProfileDto) {
        try {
            // Check if user can create more profiles
            if (!truckProfileService.canCreateMoreProfiles()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Maximum limit of 10 truck profiles reached"));
            }
            
            // Validate truck specifications
            truckProfileService.validateTruckProfile(truckProfileDto);
            
            TruckProfileDto createdProfile = truckProfileService.createTruckProfile(truckProfileDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProfile);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update truck profile", 
               description = "Updates an existing truck profile for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Truck profile updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Truck profile not found"),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    public ResponseEntity<?> updateTruckProfile(
            @Parameter(description = "Truck profile ID") @PathVariable Long id,
            @Valid @RequestBody TruckProfileDto truckProfileDto) {
        try {
            // Validate truck specifications
            truckProfileService.validateTruckProfile(truckProfileDto);
            
            TruckProfileDto updatedProfile = truckProfileService.updateTruckProfile(id, truckProfileDto);
            return ResponseEntity.ok(updatedProfile);
            
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete truck profile", 
               description = "Soft deletes a truck profile for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Truck profile deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Truck profile not found"),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    public ResponseEntity<?> deleteTruckProfile(
            @Parameter(description = "Truck profile ID") @PathVariable Long id) {
        try {
            truckProfileService.deleteTruckProfile(id);
            return ResponseEntity.noContent().build();
            
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }
    
    @GetMapping("/types/{truckType}")
    @Operation(summary = "Get truck profiles by type", 
               description = "Retrieves truck profiles filtered by truck type")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FLEET_MANAGER')")
    public ResponseEntity<List<TruckProfileDto>> getTruckProfilesByType(
            @Parameter(description = "Truck type") @PathVariable TruckProfile.TruckType truckType) {
        try {
            List<TruckProfileDto> profiles = truckProfileService.getTruckProfilesByType(truckType);
            return ResponseEntity.ok(profiles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/stats")
    @Operation(summary = "Get truck profile statistics", 
               description = "Get statistics about current user's truck profiles")
    public ResponseEntity<Map<String, Object>> getTruckProfileStats() {
        try {
            long activeCount = truckProfileService.getActiveProfileCount();
            boolean canCreateMore = truckProfileService.canCreateMoreProfiles();
            
            Map<String, Object> stats = Map.of(
                "activeProfileCount", activeCount,
                "maxProfiles", 10,
                "canCreateMore", canCreateMore,
                "remainingSlots", Math.max(0, 10 - activeCount)
            );
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/truck-types")
    @Operation(summary = "Get available truck types", 
               description = "Retrieves all available truck types for profile creation")
    public ResponseEntity<TruckProfile.TruckType[]> getTruckTypes() {
        return ResponseEntity.ok(TruckProfile.TruckType.values());
    }
    
    @GetMapping("/cargo-types")
    @Operation(summary = "Get available cargo types", 
               description = "Retrieves all available cargo types for profile creation")
    public ResponseEntity<TruckProfile.CargoType[]> getCargoTypes() {
        return ResponseEntity.ok(TruckProfile.CargoType.values());
    }
    
    @GetMapping("/emission-standards")
    @Operation(summary = "Get available emission standards", 
               description = "Retrieves all available emission standards for profile creation")
    public ResponseEntity<TruckProfile.EmissionStandard[]> getEmissionStandards() {
        return ResponseEntity.ok(TruckProfile.EmissionStandard.values());
    }
}
