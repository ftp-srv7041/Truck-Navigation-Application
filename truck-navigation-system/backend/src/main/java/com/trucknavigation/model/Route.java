package com.trucknavigation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "routes")
public class Route {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String name;
    
    private String description;
    
    // Route coordinates
    @NotNull
    @Column(name = "start_latitude")
    private BigDecimal startLatitude;
    
    @NotNull
    @Column(name = "start_longitude")
    private BigDecimal startLongitude;
    
    @NotBlank
    @Column(name = "start_address")
    private String startAddress;
    
    @NotNull
    @Column(name = "end_latitude")
    private BigDecimal endLatitude;
    
    @NotNull
    @Column(name = "end_longitude")
    private BigDecimal endLongitude;
    
    @NotBlank
    @Column(name = "end_address")
    private String endAddress;
    
    // Route details
    @Column(name = "total_distance")
    private BigDecimal totalDistance; // in kilometers
    
    @Column(name = "estimated_duration")
    private Integer estimatedDuration; // in minutes
    
    @Column(name = "estimated_fuel_cost")
    private BigDecimal estimatedFuelCost;
    
    @Column(name = "estimated_toll_cost")
    private BigDecimal estimatedTollCost;
    
    // Route geometry (encoded polyline or GeoJSON)
    @Lob
    @Column(name = "route_geometry")
    private String routeGeometry;
    
    // Waypoints and instructions
    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RouteWaypoint> waypoints;
    
    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RouteInstruction> instructions;
    
    // Associated truck profile
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "truck_profile_id")
    private TruckProfile truckProfile;
    
    // User who created/saved this route
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @Enumerated(EnumType.STRING)
    private RouteStatus status;
    
    @Enumerated(EnumType.STRING)
    private OptimizationType optimizationType;
    
    // Restrictions encountered on this route
    @Column(name = "restrictions_count")
    private Integer restrictionsCount = 0;
    
    @Column(name = "bypasses_used")
    private Integer bypassesUsed = 0;
    
    @Column(name = "traffic_level")
    private String trafficLevel; // LOW, MEDIUM, HIGH
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @Column(name = "is_favorite")
    private boolean isFavorite = false;
    
    public enum RouteStatus {
        DRAFT,
        CALCULATED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }
    
    public enum OptimizationType {
        FASTEST,
        SHORTEST,
        FUEL_EFFICIENT,
        AVOID_TOLLS,
        AVOID_RESTRICTIONS,
        BALANCED
    }
    
    // Constructors
    public Route() {}
    
    public Route(String name, BigDecimal startLatitude, BigDecimal startLongitude, 
                String startAddress, BigDecimal endLatitude, BigDecimal endLongitude, 
                String endAddress) {
        this.name = name;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.startAddress = startAddress;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.endAddress = endAddress;
        this.status = RouteStatus.DRAFT;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
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
    
    public BigDecimal getTotalDistance() {
        return totalDistance;
    }
    
    public void setTotalDistance(BigDecimal totalDistance) {
        this.totalDistance = totalDistance;
    }
    
    public Integer getEstimatedDuration() {
        return estimatedDuration;
    }
    
    public void setEstimatedDuration(Integer estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }
    
    public BigDecimal getEstimatedFuelCost() {
        return estimatedFuelCost;
    }
    
    public void setEstimatedFuelCost(BigDecimal estimatedFuelCost) {
        this.estimatedFuelCost = estimatedFuelCost;
    }
    
    public BigDecimal getEstimatedTollCost() {
        return estimatedTollCost;
    }
    
    public void setEstimatedTollCost(BigDecimal estimatedTollCost) {
        this.estimatedTollCost = estimatedTollCost;
    }
    
    public String getRouteGeometry() {
        return routeGeometry;
    }
    
    public void setRouteGeometry(String routeGeometry) {
        this.routeGeometry = routeGeometry;
    }
    
    public List<RouteWaypoint> getWaypoints() {
        return waypoints;
    }
    
    public void setWaypoints(List<RouteWaypoint> waypoints) {
        this.waypoints = waypoints;
    }
    
    public List<RouteInstruction> getInstructions() {
        return instructions;
    }
    
    public void setInstructions(List<RouteInstruction> instructions) {
        this.instructions = instructions;
    }
    
    public TruckProfile getTruckProfile() {
        return truckProfile;
    }
    
    public void setTruckProfile(TruckProfile truckProfile) {
        this.truckProfile = truckProfile;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public RouteStatus getStatus() {
        return status;
    }
    
    public void setStatus(RouteStatus status) {
        this.status = status;
    }
    
    public OptimizationType getOptimizationType() {
        return optimizationType;
    }
    
    public void setOptimizationType(OptimizationType optimizationType) {
        this.optimizationType = optimizationType;
    }
    
    public Integer getRestrictionsCount() {
        return restrictionsCount;
    }
    
    public void setRestrictionsCount(Integer restrictionsCount) {
        this.restrictionsCount = restrictionsCount;
    }
    
    public Integer getBypassesUsed() {
        return bypassesUsed;
    }
    
    public void setBypassesUsed(Integer bypassesUsed) {
        this.bypassesUsed = bypassesUsed;
    }
    
    public String getTrafficLevel() {
        return trafficLevel;
    }
    
    public void setTrafficLevel(String trafficLevel) {
        this.trafficLevel = trafficLevel;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public boolean isFavorite() {
        return isFavorite;
    }
    
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
