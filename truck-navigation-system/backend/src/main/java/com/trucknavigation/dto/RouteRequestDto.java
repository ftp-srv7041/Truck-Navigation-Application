package com.trucknavigation.dto;

import com.trucknavigation.model.Route;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class RouteRequestDto {
    
    @NotNull(message = "Start latitude is required")
    private BigDecimal startLatitude;
    
    @NotNull(message = "Start longitude is required")
    private BigDecimal startLongitude;
    
    @NotBlank(message = "Start address is required")
    private String startAddress;
    
    @NotNull(message = "End latitude is required")
    private BigDecimal endLatitude;
    
    @NotNull(message = "End longitude is required")
    private BigDecimal endLongitude;
    
    @NotBlank(message = "End address is required")
    private String endAddress;
    
    @NotNull(message = "Truck profile ID is required")
    private Long truckProfileId;
    
    private String routeName;
    
    private Route.OptimizationType optimizationType = Route.OptimizationType.BALANCED;
    
    private boolean avoidTolls = false;
    private boolean avoidHighways = false;
    private boolean includeTrafficData = true;
    
    // Intermediate waypoints (optional)
    private String[] intermediateWaypoints;
    
    // Constructors
    public RouteRequestDto() {}
    
    public RouteRequestDto(BigDecimal startLatitude, BigDecimal startLongitude, String startAddress,
                          BigDecimal endLatitude, BigDecimal endLongitude, String endAddress,
                          Long truckProfileId) {
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.startAddress = startAddress;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.endAddress = endAddress;
        this.truckProfileId = truckProfileId;
    }
    
    // Getters and Setters
    public BigDecimal getStartLatitude() {
        return startLatitude;
    }
    
    public void setStartLatitude(BigDecimal startLatitude) {
        this.startLatitude = startLatitude;
    }
    
    public BigDecimal getStartLongitude() {
        return startLongitude;
    }
    
    public void setStartLongitude(BigDecimal startLongitude) {
        this.startLongitude = startLongitude;
    }
    
    public String getStartAddress() {
        return startAddress;
    }
    
    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }
    
    public BigDecimal getEndLatitude() {
        return endLatitude;
    }
    
    public void setEndLatitude(BigDecimal endLatitude) {
        this.endLatitude = endLatitude;
    }
    
    public BigDecimal getEndLongitude() {
        return endLongitude;
    }
    
    public void setEndLongitude(BigDecimal endLongitude) {
        this.endLongitude = endLongitude;
    }
    
    public String getEndAddress() {
        return endAddress;
    }
    
    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }
    
    public Long getTruckProfileId() {
        return truckProfileId;
    }
    
    public void setTruckProfileId(Long truckProfileId) {
        this.truckProfileId = truckProfileId;
    }
    
    public String getRouteName() {
        return routeName;
    }
    
    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }
    
    public Route.OptimizationType getOptimizationType() {
        return optimizationType;
    }
    
    public void setOptimizationType(Route.OptimizationType optimizationType) {
        this.optimizationType = optimizationType;
    }
    
    public boolean isAvoidTolls() {
        return avoidTolls;
    }
    
    public void setAvoidTolls(boolean avoidTolls) {
        this.avoidTolls = avoidTolls;
    }
    
    public boolean isAvoidHighways() {
        return avoidHighways;
    }
    
    public void setAvoidHighways(boolean avoidHighways) {
        this.avoidHighways = avoidHighways;
    }
    
    public boolean isIncludeTrafficData() {
        return includeTrafficData;
    }
    
    public void setIncludeTrafficData(boolean includeTrafficData) {
        this.includeTrafficData = includeTrafficData;
    }
    
    public String[] getIntermediateWaypoints() {
        return intermediateWaypoints;
    }
    
    public void setIntermediateWaypoints(String[] intermediateWaypoints) {
        this.intermediateWaypoints = intermediateWaypoints;
    }
}
