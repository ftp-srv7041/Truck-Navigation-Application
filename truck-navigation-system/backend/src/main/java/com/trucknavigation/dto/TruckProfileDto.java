package com.trucknavigation.dto;

import com.trucknavigation.model.TruckProfile;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class TruckProfileDto {
    
    private Long id;
    
    @NotBlank(message = "Truck profile name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Height is required")
    @DecimalMin(value = "0.1", message = "Height must be at least 0.1 meters")
    private BigDecimal height;
    
    @NotNull(message = "Width is required")
    @DecimalMin(value = "0.1", message = "Width must be at least 0.1 meters")
    private BigDecimal width;
    
    @NotNull(message = "Length is required")
    @DecimalMin(value = "0.1", message = "Length must be at least 0.1 meters")
    private BigDecimal length;
    
    @NotNull(message = "Max weight is required")
    @DecimalMin(value = "0.1", message = "Weight must be at least 0.1 tonnes")
    private BigDecimal maxWeight;
    
    @NotNull(message = "Max axle load is required")
    @DecimalMin(value = "0.1", message = "Axle load must be at least 0.1 tonnes")
    private BigDecimal maxAxleLoad;
    
    @NotNull(message = "Number of axles is required")
    @Positive(message = "Number of axles must be positive")
    private Integer numberOfAxles;
    
    private TruckProfile.TruckType truckType;
    private TruckProfile.CargoType cargoType;
    private TruckProfile.EmissionStandard emissionStandard;
    
    private String registrationNumber;
    private boolean hasNationalPermit = false;
    private boolean hasOversizePermit = false;
    private boolean hasHazmatPermit = false;
    
    // Constructors
    public TruckProfileDto() {}
    
    public TruckProfileDto(String name, BigDecimal height, BigDecimal width, BigDecimal length,
                          BigDecimal maxWeight, BigDecimal maxAxleLoad, Integer numberOfAxles) {
        this.name = name;
        this.height = height;
        this.width = width;
        this.length = length;
        this.maxWeight = maxWeight;
        this.maxAxleLoad = maxAxleLoad;
        this.numberOfAxles = numberOfAxles;
    }
    
    // Static factory method to create DTO from entity
    public static TruckProfileDto fromEntity(TruckProfile truckProfile) {
        TruckProfileDto dto = new TruckProfileDto();
        dto.setId(truckProfile.getId());
        dto.setName(truckProfile.getName());
        dto.setDescription(truckProfile.getDescription());
        dto.setHeight(truckProfile.getHeight());
        dto.setWidth(truckProfile.getWidth());
        dto.setLength(truckProfile.getLength());
        dto.setMaxWeight(truckProfile.getMaxWeight());
        dto.setMaxAxleLoad(truckProfile.getMaxAxleLoad());
        dto.setNumberOfAxles(truckProfile.getNumberOfAxles());
        dto.setTruckType(truckProfile.getTruckType());
        dto.setCargoType(truckProfile.getCargoType());
        dto.setEmissionStandard(truckProfile.getEmissionStandard());
        dto.setRegistrationNumber(truckProfile.getRegistrationNumber());
        dto.setHasNationalPermit(truckProfile.isHasNationalPermit());
        dto.setHasOversizePermit(truckProfile.isHasOversizePermit());
        dto.setHasHazmatPermit(truckProfile.isHasHazmatPermit());
        return dto;
    }
    
    // Method to convert DTO to entity
    public TruckProfile toEntity() {
        TruckProfile truckProfile = new TruckProfile();
        truckProfile.setId(this.id);
        truckProfile.setName(this.name);
        truckProfile.setDescription(this.description);
        truckProfile.setHeight(this.height);
        truckProfile.setWidth(this.width);
        truckProfile.setLength(this.length);
        truckProfile.setMaxWeight(this.maxWeight);
        truckProfile.setMaxAxleLoad(this.maxAxleLoad);
        truckProfile.setNumberOfAxles(this.numberOfAxles);
        truckProfile.setTruckType(this.truckType);
        truckProfile.setCargoType(this.cargoType);
        truckProfile.setEmissionStandard(this.emissionStandard);
        truckProfile.setRegistrationNumber(this.registrationNumber);
        truckProfile.setHasNationalPermit(this.hasNationalPermit);
        truckProfile.setHasOversizePermit(this.hasOversizePermit);
        truckProfile.setHasHazmatPermit(this.hasHazmatPermit);
        return truckProfile;
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
    
    public TruckProfile.TruckType getTruckType() {
        return truckType;
    }
    
    public void setTruckType(TruckProfile.TruckType truckType) {
        this.truckType = truckType;
    }
    
    public TruckProfile.CargoType getCargoType() {
        return cargoType;
    }
    
    public void setCargoType(TruckProfile.CargoType cargoType) {
        this.cargoType = cargoType;
    }
    
    public TruckProfile.EmissionStandard getEmissionStandard() {
        return emissionStandard;
    }
    
    public void setEmissionStandard(TruckProfile.EmissionStandard emissionStandard) {
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
}
