package com.trucknavigation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "truck_profiles")
public class TruckProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String name;
    
    private String description;
    
    // Truck dimensions (in meters)
    @NotNull
    @DecimalMin(value = "0.1", message = "Height must be at least 0.1 meters")
    private BigDecimal height;
    
    @NotNull
    @DecimalMin(value = "0.1", message = "Width must be at least 0.1 meters")
    private BigDecimal width;
    
    @NotNull
    @DecimalMin(value = "0.1", message = "Length must be at least 0.1 meters")
    private BigDecimal length;
    
    // Weight specifications (in tonnes)
    @NotNull
    @DecimalMin(value = "0.1", message = "Weight must be at least 0.1 tonnes")
    private BigDecimal maxWeight;
    
    @NotNull
    @DecimalMin(value = "0.1", message = "Axle load must be at least 0.1 tonnes")
    private BigDecimal maxAxleLoad;
    
    @NotNull
    @Positive
    private Integer numberOfAxles;
    
    @Enumerated(EnumType.STRING)
    private TruckType truckType;
    
    @Enumerated(EnumType.STRING)
    private CargoType cargoType;
    
    // Engine specifications for emissions restrictions
    @Enumerated(EnumType.STRING)
    private EmissionStandard emissionStandard;
    
    // Registration and permit details
    private String registrationNumber;
    
    @Column(name = "has_national_permit")
    private boolean hasNationalPermit = false;
    
    @Column(name = "has_oversize_permit")
    private boolean hasOversizePermit = false;
    
    @Column(name = "has_hazmat_permit")
    private boolean hasHazmatPermit = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @Column(name = "is_active")
    private boolean isActive = true;
    
    public enum TruckType {
        MINI_TRUCK,      // Tata Ace, Mahindra Pickup
        LIGHT_TRUCK,     // Up to 7.5 tonnes
        MEDIUM_TRUCK,    // 7.5 to 16 tonnes
        HEAVY_TRUCK,     // 16 to 25 tonnes
        MULTI_AXLE,      // Above 25 tonnes
        TRAILER,         // Articulated vehicles
        CONTAINER,       // Container carriers
        TANKER,          // Liquid transport
        FLATBED,         // Open cargo
        REFRIGERATED     // Cold chain
    }
    
    public enum CargoType {
        GENERAL,
        HAZARDOUS,
        PERISHABLE,
        OVERSIZED,
        LIQUID,
        FRAGILE,
        LIVESTOCK,
        CONSTRUCTION_MATERIAL,
        CONTAINER,
        BULK_CARGO
    }
    
    public enum EmissionStandard {
        BS_III,
        BS_IV,
        BS_VI,
        ELECTRIC,
        CNG,
        LNG
    }
    
    // Constructors
    public TruckProfile() {}
    
    public TruckProfile(String name, BigDecimal height, BigDecimal width, BigDecimal length, 
                       BigDecimal maxWeight, BigDecimal maxAxleLoad, Integer numberOfAxles) {
        this.name = name;
        this.height = height;
        this.width = width;
        this.length = length;
        this.maxWeight = maxWeight;
        this.maxAxleLoad = maxAxleLoad;
        this.numberOfAxles = numberOfAxles;
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
    
    public BigDecimal getHeight() {
        return height;
    }
    
    public void setHeight(BigDecimal height) {
        this.height = height;
    }
    
    public BigDecimal getWidth() {
        return width;
    }
    
    public void setWidth(BigDecimal width) {
        this.width = width;
    }
    
    public BigDecimal getLength() {
        return length;
    }
    
    public void setLength(BigDecimal length) {
        this.length = length;
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
    
    public Integer getNumberOfAxles() {
        return numberOfAxles;
    }
    
    public void setNumberOfAxles(Integer numberOfAxles) {
        this.numberOfAxles = numberOfAxles;
    }
    
    public TruckType getTruckType() {
        return truckType;
    }
    
    public void setTruckType(TruckType truckType) {
        this.truckType = truckType;
    }
    
    public CargoType getCargoType() {
        return cargoType;
    }
    
    public void setCargoType(CargoType cargoType) {
        this.cargoType = cargoType;
    }
    
    public EmissionStandard getEmissionStandard() {
        return emissionStandard;
    }
    
    public void setEmissionStandard(EmissionStandard emissionStandard) {
        this.emissionStandard = emissionStandard;
    }
    
    public String getRegistrationNumber() {
        return registrationNumber;
    }
    
    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
    
    public boolean isHasNationalPermit() {
        return hasNationalPermit;
    }
    
    public void setHasNationalPermit(boolean hasNationalPermit) {
        this.hasNationalPermit = hasNationalPermit;
    }
    
    public boolean isHasOversizePermit() {
        return hasOversizePermit;
    }
    
    public void setHasOversizePermit(boolean hasOversizePermit) {
        this.hasOversizePermit = hasOversizePermit;
    }
    
    public boolean isHasHazmatPermit() {
        return hasHazmatPermit;
    }
    
    public void setHasHazmatPermit(boolean hasHazmatPermit) {
        this.hasHazmatPermit = hasHazmatPermit;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
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
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
