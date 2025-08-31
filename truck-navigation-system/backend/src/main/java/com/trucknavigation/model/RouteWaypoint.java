package com.trucknavigation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "route_waypoints")
public class RouteWaypoint {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;
    
    @NotNull
    @Column(name = "sequence_order")
    private Integer sequenceOrder;
    
    @NotNull
    private BigDecimal latitude;
    
    @NotNull
    private BigDecimal longitude;
    
    private String address;
    private String landmark;
    
    @Enumerated(EnumType.STRING)
    private WaypointType waypointType;
    
    // Distance from previous waypoint (in km)
    @Column(name = "distance_from_previous")
    private BigDecimal distanceFromPrevious;
    
    // Duration from previous waypoint (in minutes)
    @Column(name = "duration_from_previous")
    private Integer durationFromPrevious;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    public enum WaypointType {
        START,
        END,
        REST_STOP,
        FUEL_STATION,
        TOLL_PLAZA,
        WEIGH_BRIDGE,
        BYPASS_ENTRY,
        BYPASS_EXIT,
        INTERMEDIATE,
        RESTRICTION_POINT
    }
    
    // Constructors
    public RouteWaypoint() {}
    
    public RouteWaypoint(Route route, Integer sequenceOrder, BigDecimal latitude, 
                        BigDecimal longitude, WaypointType waypointType) {
        this.route = route;
        this.sequenceOrder = sequenceOrder;
        this.latitude = latitude;
        this.longitude = longitude;
        this.waypointType = waypointType;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Route getRoute() {
        return route;
    }
    
    public void setRoute(Route route) {
        this.route = route;
    }
    
    public Integer getSequenceOrder() {
        return sequenceOrder;
    }
    
    public void setSequenceOrder(Integer sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
    }
    
    public BigDecimal getLatitude() {
        return latitude;
    }
    
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }
    
    public BigDecimal getLongitude() {
        return longitude;
    }
    
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getLandmark() {
        return landmark;
    }
    
    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }
    
    public WaypointType getWaypointType() {
        return waypointType;
    }
    
    public void setWaypointType(WaypointType waypointType) {
        this.waypointType = waypointType;
    }
    
    public BigDecimal getDistanceFromPrevious() {
        return distanceFromPrevious;
    }
    
    public void setDistanceFromPrevious(BigDecimal distanceFromPrevious) {
        this.distanceFromPrevious = distanceFromPrevious;
    }
    
    public Integer getDurationFromPrevious() {
        return durationFromPrevious;
    }
    
    public void setDurationFromPrevious(Integer durationFromPrevious) {
        this.durationFromPrevious = durationFromPrevious;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
