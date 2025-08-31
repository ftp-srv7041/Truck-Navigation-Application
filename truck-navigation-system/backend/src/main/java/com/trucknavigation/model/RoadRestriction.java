package com.trucknavigation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "road_restrictions")
public class RoadRestriction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String name;
    
    private String description;
    
    @NotNull
    private BigDecimal latitude;
    
    @NotNull
    private BigDecimal longitude;
    
    // Road/Bridge/Tunnel identification
    private String roadNumber;
    private String highway;
    private String city;
    private String state;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private RestrictionType restrictionType;
    
    // Physical restrictions
    private BigDecimal maxHeight;
    private BigDecimal maxWidth;
    private BigDecimal maxLength;
    private BigDecimal maxWeight;
    private BigDecimal maxAxleLoad;
    
    // Time-based restrictions
    @Column(name = "restriction_start_time")
    private LocalTime restrictionStartTime;
    
    @Column(name = "restriction_end_time")
    private LocalTime restrictionEndTime;
    
    // Days of week restrictions (comma-separated: MON,TUE,WED...)
    private String restrictedDays;
    
    @Enumerated(EnumType.STRING)
    private Severity severity;
    
    // Additional flags
    @Column(name = "trucks_prohibited")
    private boolean trucksProhibited = false;
    
    @Column(name = "hazmat_prohibited")
    private boolean hazmatProhibited = false;
    
    @Column(name = "oversize_prohibited")
    private boolean oversizeProhibited = false;
    
    @Column(name = "night_restriction")
    private boolean nightRestriction = false;
    
    // Alternative route information
    private String alternativeRoute;
    private String bypassRoute;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @Column(name = "is_active")
    private boolean isActive = true;
    
    // Data source tracking
    private String dataSource;
    private LocalDateTime lastVerified;
    
    public enum RestrictionType {
        BRIDGE_HEIGHT,
        BRIDGE_WEIGHT,
        TUNNEL_HEIGHT,
        TUNNEL_WIDTH,
        ROAD_WIDTH,
        ROAD_WEIGHT,
        NO_ENTRY_ZONE,
        TIME_RESTRICTION,
        RAILWAY_CROSSING,
        TOLL_PLAZA,
        WEIGH_BRIDGE,
        ENVIRONMENTAL_ZONE,
        URBAN_RESTRICTION,
        INTERSTATE_BORDER
    }
    
    public enum Severity {
        LOW,      // Advisory
        MEDIUM,   // Strong recommendation
        HIGH,     // Mandatory avoidance
        CRITICAL  // Physical impossibility/legal violation
    }
    
    // Constructors
    public RoadRestriction() {}
    
    public RoadRestriction(String name, BigDecimal latitude, BigDecimal longitude, 
                          RestrictionType restrictionType) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.restrictionType = restrictionType;
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
    
    public String getRoadNumber() {
        return roadNumber;
    }
    
    public void setRoadNumber(String roadNumber) {
        this.roadNumber = roadNumber;
    }
    
    public String getHighway() {
        return highway;
    }
    
    public void setHighway(String highway) {
        this.highway = highway;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public RestrictionType getRestrictionType() {
        return restrictionType;
    }
    
    public void setRestrictionType(RestrictionType restrictionType) {
        this.restrictionType = restrictionType;
    }
    
    public BigDecimal getMaxHeight() {
        return maxHeight;
    }
    
    public void setMaxHeight(BigDecimal maxHeight) {
        this.maxHeight = maxHeight;
    }
    
    public BigDecimal getMaxWidth() {
        return maxWidth;
    }
    
    public void setMaxWidth(BigDecimal maxWidth) {
        this.maxWidth = maxWidth;
    }
    
    public BigDecimal getMaxLength() {
        return maxLength;
    }
    
    public void setMaxLength(BigDecimal maxLength) {
        this.maxLength = maxLength;
    }
    
    public BigDecimal getMaxWeight() {
        return maxWeight;
    }
    
    public void setMaxWeight(BigDecimal maxWeight) {
        this.maxWeight = maxWeight;
    }
    
    public BigDecimal getMaxAxleLoad() {
        return maxAxleLoad;
    }
    
    public void setMaxAxleLoad(BigDecimal maxAxleLoad) {
        this.maxAxleLoad = maxAxleLoad;
    }
    
    public LocalTime getRestrictionStartTime() {
        return restrictionStartTime;
    }
    
    public void setRestrictionStartTime(LocalTime restrictionStartTime) {
        this.restrictionStartTime = restrictionStartTime;
    }
    
    public LocalTime getRestrictionEndTime() {
        return restrictionEndTime;
    }
    
    public void setRestrictionEndTime(LocalTime restrictionEndTime) {
        this.restrictionEndTime = restrictionEndTime;
    }
    
    public String getRestrictedDays() {
        return restrictedDays;
    }
    
    public void setRestrictedDays(String restrictedDays) {
        this.restrictedDays = restrictedDays;
    }
    
    public Severity getSeverity() {
        return severity;
    }
    
    public void setSeverity(Severity severity) {
        this.severity = severity;
    }
    
    public boolean isTrucksProhibited() {
        return trucksProhibited;
    }
    
    public void setTrucksProhibited(boolean trucksProhibited) {
        this.trucksProhibited = trucksProhibited;
    }
    
    public boolean isHazmatProhibited() {
        return hazmatProhibited;
    }
    
    public void setHazmatProhibited(boolean hazmatProhibited) {
        this.hazmatProhibited = hazmatProhibited;
    }
    
    public boolean isOversizeProhibited() {
        return oversizeProhibited;
    }
    
    public void setOversizeProhibited(boolean oversizeProhibited) {
        this.oversizeProhibited = oversizeProhibited;
    }
    
    public boolean isNightRestriction() {
        return nightRestriction;
    }
    
    public void setNightRestriction(boolean nightRestriction) {
        this.nightRestriction = nightRestriction;
    }
    
    public String getAlternativeRoute() {
        return alternativeRoute;
    }
    
    public void setAlternativeRoute(String alternativeRoute) {
        this.alternativeRoute = alternativeRoute;
    }
    
    public String getBypassRoute() {
        return bypassRoute;
    }
    
    public void setBypassRoute(String bypassRoute) {
        this.bypassRoute = bypassRoute;
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
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public String getDataSource() {
        return dataSource;
    }
    
    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
    
    public LocalDateTime getLastVerified() {
        return lastVerified;
    }
    
    public void setLastVerified(LocalDateTime lastVerified) {
        this.lastVerified = lastVerified;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
