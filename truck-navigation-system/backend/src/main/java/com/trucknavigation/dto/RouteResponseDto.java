package com.trucknavigation.dto;

import com.trucknavigation.model.Route;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class RouteResponseDto {
    
    private List<RouteOption> routeOptions;
    private int restrictionsFound;
    private Long truckProfileUsed;
    private LocalDateTime calculatedAt;
    
    public static class RouteOption {
        private String name;
        private String description;
        private Route.OptimizationType optimizationType;
        private BigDecimal totalDistance;
        private Integer estimatedDuration;
        private BigDecimal estimatedFuelCost;
        private BigDecimal estimatedTollCost;
        private String routeGeometry;
        private Integer restrictionsCount;
        private Integer bypassesUsed;
        private String trafficLevel;
        private List<String> warnings;
        private List<String> recommendations;
        
        // Constructors
        public RouteOption() {}
        
        // Getters and Setters
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
        
        public Route.OptimizationType getOptimizationType() {
            return optimizationType;
        }
        
        public void setOptimizationType(Route.OptimizationType optimizationType) {
            this.optimizationType = optimizationType;
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
        
        public List<String> getWarnings() {
            return warnings;
        }
        
        public void setWarnings(List<String> warnings) {
            this.warnings = warnings;
        }
        
        public List<String> getRecommendations() {
            return recommendations;
        }
        
        public void setRecommendations(List<String> recommendations) {
            this.recommendations = recommendations;
        }
        
        // Helper methods for cost calculations
        public BigDecimal getTotalEstimatedCost() {
            BigDecimal fuel = estimatedFuelCost != null ? estimatedFuelCost : BigDecimal.ZERO;
            BigDecimal toll = estimatedTollCost != null ? estimatedTollCost : BigDecimal.ZERO;
            return fuel.add(toll);
        }
        
        public String getFormattedDuration() {
            if (estimatedDuration == null) return "N/A";
            
            int hours = estimatedDuration / 60;
            int minutes = estimatedDuration % 60;
            
            if (hours > 0) {
                return String.format("%d hours %d minutes", hours, minutes);
            } else {
                return String.format("%d minutes", minutes);
            }
        }
        
        public String getFormattedDistance() {
            if (totalDistance == null) return "N/A";
            return String.format("%.1f km", totalDistance.doubleValue());
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RouteOption that = (RouteOption) o;
            return Objects.equals(optimizationType, that.optimizationType) &&
                   Objects.equals(totalDistance, that.totalDistance) &&
                   Objects.equals(estimatedDuration, that.estimatedDuration);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(optimizationType, totalDistance, estimatedDuration);
        }
    }
    
    // Constructors
    public RouteResponseDto() {}
    
    // Getters and Setters
    public List<RouteOption> getRouteOptions() {
        return routeOptions;
    }
    
    public void setRouteOptions(List<RouteOption> routeOptions) {
        this.routeOptions = routeOptions;
    }
    
    public int getRestrictionsFound() {
        return restrictionsFound;
    }
    
    public void setRestrictionsFound(int restrictionsFound) {
        this.restrictionsFound = restrictionsFound;
    }
    
    public Long getTruckProfileUsed() {
        return truckProfileUsed;
    }
    
    public void setTruckProfileUsed(Long truckProfileUsed) {
        this.truckProfileUsed = truckProfileUsed;
    }
    
    public LocalDateTime getCalculatedAt() {
        return calculatedAt;
    }
    
    public void setCalculatedAt(LocalDateTime calculatedAt) {
        this.calculatedAt = calculatedAt;
    }
    
    // Helper methods
    public RouteOption getBestOption() {
        if (routeOptions == null || routeOptions.isEmpty()) {
            return null;
        }
        
        // Return the option with the best balance of time and cost
        return routeOptions.stream()
                .min((o1, o2) -> {
                    // Score based on duration and total cost
                    double score1 = o1.getEstimatedDuration() + (o1.getTotalEstimatedCost().doubleValue() / 10);
                    double score2 = o2.getEstimatedDuration() + (o2.getTotalEstimatedCost().doubleValue() / 10);
                    return Double.compare(score1, score2);
                })
                .orElse(routeOptions.get(0));
    }
    
    public boolean hasRestrictions() {
        return restrictionsFound > 0;
    }
}
