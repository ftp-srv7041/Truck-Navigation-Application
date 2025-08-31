package com.trucknavigation.controller;

import com.trucknavigation.dto.RouteRequestDto;
import com.trucknavigation.dto.RouteResponseDto;
import com.trucknavigation.service.RouteCalculationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/routes")
@Tag(name = "Route Calculation", description = "APIs for calculating truck-optimized routes")
public class RouteController {
    
    @Autowired
    private RouteCalculationService routeCalculationService;
    
    @PostMapping("/calculate")
    @Operation(summary = "Calculate truck-optimized route", 
               description = "Calculates multiple route options considering truck restrictions and traffic")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Route calculated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "404", description = "Truck profile not found")
    })
    public ResponseEntity<?> calculateRoute(@Valid @RequestBody RouteRequestDto routeRequest) {
        try {
            RouteResponseDto response = routeCalculationService.calculateRoute(routeRequest);
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred during route calculation"));
        }
    }
    
    @GetMapping("/test")
    @Operation(summary = "Test route calculation endpoint", 
               description = "Simple test endpoint to verify route calculation service")
    public ResponseEntity<Map<String, String>> testRouteCalculation() {
        return ResponseEntity.ok(Map.of(
            "status", "Route calculation service is running",
            "timestamp", java.time.LocalDateTime.now().toString(),
            "version", "1.0.0"
        ));
    }
}
