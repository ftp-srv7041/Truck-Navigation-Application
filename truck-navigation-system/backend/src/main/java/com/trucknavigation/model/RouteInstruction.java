package com.trucknavigation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "route_instructions")
public class RouteInstruction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;
    
    @NotNull
    @Column(name = "sequence_order")
    private Integer sequenceOrder;
    
    @NotBlank
    private String instruction;
    
    // Location where this instruction applies
    @NotNull
    private BigDecimal latitude;
    
    @NotNull
    private BigDecimal longitude;
    
    @Enumerated(EnumType.STRING)
    private InstructionType instructionType;
    
    // Distance to this instruction from route start (in km)
    @Column(name = "distance_from_start")
    private BigDecimal distanceFromStart;
    
    // Duration to this instruction from route start (in minutes)
    @Column(name = "duration_from_start")
    private Integer durationFromStart;
    
    // Distance for this maneuver (in km)
    @Column(name = "maneuver_distance")
    private BigDecimal maneuverDistance;
    
    // Road information
    @Column(name = "road_name")
    private String roadName;
    
    @Column(name = "road_number")
    private String roadNumber;
    
    // Turn direction and angle
    @Column(name = "turn_angle")
    private Integer turnAngle; // degrees
    
    @Enumerated(EnumType.STRING)
    private TurnDirection turnDirection;
    
    // Special instructions for trucks
    @Column(name = "truck_specific_note")
    private String truckSpecificNote;
    
    @Column(name = "restriction_warning")
    private String restrictionWarning;
    
    @Column(name = "bypass_instruction")
    private String bypassInstruction;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    public enum InstructionType {
        START,
        END,
        TURN_LEFT,
        TURN_RIGHT,
        STRAIGHT,
        SLIGHT_LEFT,
        SLIGHT_RIGHT,
        SHARP_LEFT,
        SHARP_RIGHT,
        U_TURN,
        ROUNDABOUT_ENTRY,
        ROUNDABOUT_EXIT,
        MERGE,
        FORK,
        RAMP_ENTRY,
        RAMP_EXIT,
        TOLL_PLAZA,
        REST_STOP,
        FUEL_STOP,
        RESTRICTION_WARNING,
        BYPASS_ROUTE
    }
    
    public enum TurnDirection {
        LEFT,
        RIGHT,
        STRAIGHT,
        U_TURN,
        ROUNDABOUT
    }
    
    // Constructors
    public RouteInstruction() {}
    
    public RouteInstruction(Route route, Integer sequenceOrder, String instruction, 
                           BigDecimal latitude, BigDecimal longitude, InstructionType instructionType) {
        this.route = route;
        this.sequenceOrder = sequenceOrder;
        this.instruction = instruction;
        this.latitude = latitude;
        this.longitude = longitude;
        this.instructionType = instructionType;
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
    
    public String getInstruction() {
        return instruction;
    }
    
    public void setInstruction(String instruction) {
        this.instruction = instruction;
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
    
    public InstructionType getInstructionType() {
        return instructionType;
    }
    
    public void setInstructionType(InstructionType instructionType) {
        this.instructionType = instructionType;
    }
    
    public BigDecimal getDistanceFromStart() {
        return distanceFromStart;
    }
    
    public void setDistanceFromStart(BigDecimal distanceFromStart) {
        this.distanceFromStart = distanceFromStart;
    }
    
    public Integer getDurationFromStart() {
        return durationFromStart;
    }
    
    public void setDurationFromStart(Integer durationFromStart) {
        this.durationFromStart = durationFromStart;
    }
    
    public BigDecimal getManeuverDistance() {
        return maneuverDistance;
    }
    
    public void setManeuverDistance(BigDecimal maneuverDistance) {
        this.maneuverDistance = maneuverDistance;
    }
    
    public String getRoadName() {
        return roadName;
    }
    
    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }
    
    public String getRoadNumber() {
        return roadNumber;
    }
    
    public void setRoadNumber(String roadNumber) {
        this.roadNumber = roadNumber;
    }
    
    public Integer getTurnAngle() {
        return turnAngle;
    }
    
    public void setTurnAngle(Integer turnAngle) {
        this.turnAngle = turnAngle;
    }
    
    public TurnDirection getTurnDirection() {
        return turnDirection;
    }
    
    public void setTurnDirection(TurnDirection turnDirection) {
        this.turnDirection = turnDirection;
    }
    
    public String getTruckSpecificNote() {
        return truckSpecificNote;
    }
    
    public void setTruckSpecificNote(String truckSpecificNote) {
        this.truckSpecificNote = truckSpecificNote;
    }
    
    public String getRestrictionWarning() {
        return restrictionWarning;
    }
    
    public void setRestrictionWarning(String restrictionWarning) {
        this.restrictionWarning = restrictionWarning;
    }
    
    public String getBypassInstruction() {
        return bypassInstruction;
    }
    
    public void setBypassInstruction(String bypassInstruction) {
        this.bypassInstruction = bypassInstruction;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
