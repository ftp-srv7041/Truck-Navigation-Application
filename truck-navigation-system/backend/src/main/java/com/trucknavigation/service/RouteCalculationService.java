package com.trucknavigation.service;

import com.trucknavigation.dto.RouteRequestDto;
import com.trucknavigation.dto.RouteResponseDto;
import com.trucknavigation.model.*;
import com.trucknavigation.repository.RoadRestrictionRepository;
import com.trucknavigation.repository.RouteRepository;
import com.trucknavigation.repository.TruckProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class RouteCalculationService {
    
    @Autowired
    private RouteRepository routeRepository;
    
    @Autowired
    private TruckProfileRepository truckProfileRepository;
    
    @Autowired
    private RoadRestrictionRepository roadRestrictionRepository;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${truck-navigation.maps.api-url}")
    private String mapsApiUrl;
    
    @Value("${truck-navigation.maps.api-key}")
    private String mapsApiKey;
    
    @Value("${truck-navigation.routing.default-speed}")
    private int defaultSpeed;
    
    @Value("${truck-navigation.routing.max-route-distance}")
    private int maxRouteDistance;
    
    /**
     * Calculate truck-optimized route between two points
     */
    public RouteResponseDto calculateRoute(RouteRequestDto routeRequest) {
        // Validate input
        validateRouteRequest(routeRequest);
        
        // Get truck profile
        TruckProfile truckProfile = truckProfileRepository.findById(routeRequest.getTruckProfileId())
                .orElseThrow(() -> new RuntimeException("Truck profile not found"));
        
        // Find applicable road restrictions
        List<RoadRestriction> restrictions = findApplicableRestrictions(
                routeRequest.getStartLatitude(), routeRequest.getStartLongitude(),
                routeRequest.getEndLatitude(), routeRequest.getEndLongitude(),
                truckProfile
        );
        
        // Calculate multiple route options
        List<RouteResponseDto.RouteOption> routeOptions = calculateRouteOptions(
                routeRequest, truckProfile, restrictions
        );
        
        // Create response
        RouteResponseDto response = new RouteResponseDto();
        response.setRouteOptions(routeOptions);
        response.setRestrictionsFound(restrictions.size());
        response.setTruckProfileUsed(routeRequest.getTruckProfileId());
        response.setCalculatedAt(LocalDateTime.now());
        
        return response;
    }
    
    /**
     * Find road restrictions that affect the route
     */
    public List<RoadRestriction> findApplicableRestrictions(
            BigDecimal startLat, BigDecimal startLon, 
            BigDecimal endLat, BigDecimal endLon, 
            TruckProfile truckProfile) {
        
        // Calculate bounding box for the route area (with some buffer)
        BigDecimal minLat = startLat.min(endLat).subtract(BigDecimal.valueOf(0.1));
        BigDecimal maxLat = startLat.max(endLat).add(BigDecimal.valueOf(0.1));
        BigDecimal minLon = startLon.min(endLon).subtract(BigDecimal.valueOf(0.1));
        BigDecimal maxLon = startLon.max(endLon).add(BigDecimal.valueOf(0.1));
        
        // Get all restrictions in the area
        List<RoadRestriction> areaRestrictions = roadRestrictionRepository
                .findRestrictionsInArea(minLat, maxLat, minLon, maxLon);
        
        // Filter restrictions that apply to this truck
        return areaRestrictions.stream()
                .filter(restriction -> isRestrictionApplicable(restriction, truckProfile))
                .collect(Collectors.toList());
    }
    
    /**
     * Check if a road restriction applies to the given truck profile
     */
    private boolean isRestrictionApplicable(RoadRestriction restriction, TruckProfile truckProfile) {
        // Check physical restrictions
        if (restriction.getMaxHeight() != null && 
            truckProfile.getHeight().compareTo(restriction.getMaxHeight()) > 0) {
            return true;
        }
        
        if (restriction.getMaxWidth() != null && 
            truckProfile.getWidth().compareTo(restriction.getMaxWidth()) > 0) {
            return true;
        }
        
        if (restriction.getMaxLength() != null && 
            truckProfile.getLength().compareTo(restriction.getMaxLength()) > 0) {
            return true;
        }
        
        if (restriction.getMaxWeight() != null && 
            truckProfile.getMaxWeight().compareTo(restriction.getMaxWeight()) > 0) {
            return true;
        }
        
        if (restriction.getMaxAxleLoad() != null && 
            truckProfile.getMaxAxleLoad().compareTo(restriction.getMaxAxleLoad()) > 0) {
            return true;
        }
        
        // Check categorical restrictions
        if (restriction.isTrucksProhibited()) {
            return true;
        }
        
        if (restriction.isHazmatProhibited() && truckProfile.isHasHazmatPermit()) {
            return true;
        }
        
        if (restriction.isOversizeProhibited() && truckProfile.isHasOversizePermit()) {
            return true;
        }
        
        // Check time-based restrictions
        if (restriction.getRestrictionStartTime() != null && restriction.getRestrictionEndTime() != null) {
            LocalTime currentTime = LocalTime.now();
            if (isTimeInRestrictionPeriod(currentTime, restriction.getRestrictionStartTime(), 
                                        restriction.getRestrictionEndTime())) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Calculate multiple route options considering different optimization criteria
     */
    private List<RouteResponseDto.RouteOption> calculateRouteOptions(
            RouteRequestDto routeRequest, TruckProfile truckProfile, 
            List<RoadRestriction> restrictions) {
        
        List<RouteResponseDto.RouteOption> options = new ArrayList<>();
        
        // Calculate different route options
        options.add(calculateFastestRoute(routeRequest, truckProfile, restrictions));
        options.add(calculateShortestRoute(routeRequest, truckProfile, restrictions));
        options.add(calculateFuelEfficientRoute(routeRequest, truckProfile, restrictions));
        
        if (routeRequest.getOptimizationType() == Route.OptimizationType.AVOID_TOLLS) {
            options.add(calculateTollFreeRoute(routeRequest, truckProfile, restrictions));
        }
        
        // Remove duplicate routes and sort by preference
        return options.stream()
                .distinct()
                .sorted(Comparator.comparing(RouteResponseDto.RouteOption::getEstimatedDuration))
                .collect(Collectors.toList());
    }
    
    /**
     * Calculate fastest route considering truck restrictions
     */
    private RouteResponseDto.RouteOption calculateFastestRoute(
            RouteRequestDto routeRequest, TruckProfile truckProfile, 
            List<RoadRestriction> restrictions) {
        
        RouteResponseDto.RouteOption option = new RouteResponseDto.RouteOption();
        option.setName("Fastest Route");
        option.setDescription("Optimized for minimum travel time");
        option.setOptimizationType(Route.OptimizationType.FASTEST);
        
        // Simulate route calculation (in real implementation, call external routing API)
        double distance = calculateHaversineDistance(
                routeRequest.getStartLatitude(), routeRequest.getStartLongitude(),
                routeRequest.getEndLatitude(), routeRequest.getEndLongitude()
        );
        
        // Add buffer for actual road distance (typically 1.3x of straight-line distance)
        distance *= 1.3;
        
        // Calculate duration considering truck speed limitations
        int avgSpeed = calculateAverageSpeed(truckProfile, restrictions);
        int duration = (int) ((distance / avgSpeed) * 60); // Convert to minutes
        
        // Estimate costs
        BigDecimal fuelCost = calculateFuelCost(distance, truckProfile);
        BigDecimal tollCost = calculateTollCost(distance);
        
        option.setTotalDistance(BigDecimal.valueOf(distance));
        option.setEstimatedDuration(duration);
        option.setEstimatedFuelCost(fuelCost);
        option.setEstimatedTollCost(tollCost);
        option.setRestrictionsCount(restrictions.size());
        option.setBypassesUsed(restrictions.size() > 0 ? 1 : 0);
        option.setTrafficLevel("MEDIUM");
        
        // Generate sample route geometry (in real implementation, get from routing API)
        option.setRouteGeometry(generateSampleRouteGeometry(routeRequest));
        
        return option;
    }
    
    /**
     * Calculate shortest route
     */
    private RouteResponseDto.RouteOption calculateShortestRoute(
            RouteRequestDto routeRequest, TruckProfile truckProfile, 
            List<RoadRestriction> restrictions) {
        
        RouteResponseDto.RouteOption option = new RouteResponseDto.RouteOption();
        option.setName("Shortest Route");
        option.setDescription("Optimized for minimum distance");
        option.setOptimizationType(Route.OptimizationType.SHORTEST);
        
        double distance = calculateHaversineDistance(
                routeRequest.getStartLatitude(), routeRequest.getStartLongitude(),
                routeRequest.getEndLatitude(), routeRequest.getEndLongitude()
        );
        
        distance *= 1.15; // Shorter multiplier for shortest route
        
        int avgSpeed = calculateAverageSpeed(truckProfile, restrictions) - 5; // Slower on shorter routes
        int duration = (int) ((distance / avgSpeed) * 60);
        
        BigDecimal fuelCost = calculateFuelCost(distance, truckProfile);
        BigDecimal tollCost = calculateTollCost(distance);
        
        option.setTotalDistance(BigDecimal.valueOf(distance));
        option.setEstimatedDuration(duration);
        option.setEstimatedFuelCost(fuelCost);
        option.setEstimatedTollCost(tollCost);
        option.setRestrictionsCount(restrictions.size());
        option.setBypassesUsed(0);
        option.setTrafficLevel("LOW");
        option.setRouteGeometry(generateSampleRouteGeometry(routeRequest));
        
        return option;
    }
    
    /**
     * Calculate fuel-efficient route
     */
    private RouteResponseDto.RouteOption calculateFuelEfficientRoute(
            RouteRequestDto routeRequest, TruckProfile truckProfile, 
            List<RoadRestriction> restrictions) {
        
        RouteResponseDto.RouteOption option = new RouteResponseDto.RouteOption();
        option.setName("Fuel Efficient Route");
        option.setDescription("Optimized for minimum fuel consumption");
        option.setOptimizationType(Route.OptimizationType.FUEL_EFFICIENT);
        
        double distance = calculateHaversineDistance(
                routeRequest.getStartLatitude(), routeRequest.getStartLongitude(),
                routeRequest.getEndLatitude(), routeRequest.getEndLongitude()
        );
        
        distance *= 1.4; // Longer route but more efficient
        
        int avgSpeed = calculateAverageSpeed(truckProfile, restrictions) + 5; // Higher speed on highways
        int duration = (int) ((distance / avgSpeed) * 60);
        
        BigDecimal fuelCost = calculateFuelCost(distance, truckProfile).multiply(BigDecimal.valueOf(0.85)); // 15% savings
        BigDecimal tollCost = calculateTollCost(distance);
        
        option.setTotalDistance(BigDecimal.valueOf(distance));
        option.setEstimatedDuration(duration);
        option.setEstimatedFuelCost(fuelCost);
        option.setEstimatedTollCost(tollCost);
        option.setRestrictionsCount(restrictions.size());
        option.setBypassesUsed(2);
        option.setTrafficLevel("LOW");
        option.setRouteGeometry(generateSampleRouteGeometry(routeRequest));
        
        return option;
    }
    
    /**
     * Calculate toll-free route
     */
    private RouteResponseDto.RouteOption calculateTollFreeRoute(
            RouteRequestDto routeRequest, TruckProfile truckProfile, 
            List<RoadRestriction> restrictions) {
        
        RouteResponseDto.RouteOption option = new RouteResponseDto.RouteOption();
        option.setName("Toll-Free Route");
        option.setDescription("Avoids toll roads and highways");
        option.setOptimizationType(Route.OptimizationType.AVOID_TOLLS);
        
        double distance = calculateHaversineDistance(
                routeRequest.getStartLatitude(), routeRequest.getStartLongitude(),
                routeRequest.getEndLatitude(), routeRequest.getEndLongitude()
        );
        
        distance *= 1.6; // Much longer route avoiding tolls
        
        int avgSpeed = calculateAverageSpeed(truckProfile, restrictions) - 10; // Slower on local roads
        int duration = (int) ((distance / avgSpeed) * 60);
        
        BigDecimal fuelCost = calculateFuelCost(distance, truckProfile);
        BigDecimal tollCost = BigDecimal.ZERO; // No tolls
        
        option.setTotalDistance(BigDecimal.valueOf(distance));
        option.setEstimatedDuration(duration);
        option.setEstimatedFuelCost(fuelCost);
        option.setEstimatedTollCost(tollCost);
        option.setRestrictionsCount(restrictions.size() + 2); // More restrictions on local roads
        option.setBypassesUsed(3);
        option.setTrafficLevel("HIGH");
        option.setRouteGeometry(generateSampleRouteGeometry(routeRequest));
        
        return option;
    }
    
    /**
     * Save a calculated route for the user
     */
    public Route saveRoute(RouteRequestDto routeRequest, RouteResponseDto.RouteOption selectedOption, User user) {
        TruckProfile truckProfile = truckProfileRepository.findById(routeRequest.getTruckProfileId())
                .orElseThrow(() -> new RuntimeException("Truck profile not found"));
        
        Route route = new Route();
        route.setName(routeRequest.getRouteName() != null ? routeRequest.getRouteName() : 
                     "Route from " + routeRequest.getStartAddress() + " to " + routeRequest.getEndAddress());
        route.setStartLatitude(routeRequest.getStartLatitude());
        route.setStartLongitude(routeRequest.getStartLongitude());
        route.setStartAddress(routeRequest.getStartAddress());
        route.setEndLatitude(routeRequest.getEndLatitude());
        route.setEndLongitude(routeRequest.getEndLongitude());
        route.setEndAddress(routeRequest.getEndAddress());
        route.setTotalDistance(selectedOption.getTotalDistance());
        route.setEstimatedDuration(selectedOption.getEstimatedDuration());
        route.setEstimatedFuelCost(selectedOption.getEstimatedFuelCost());
        route.setEstimatedTollCost(selectedOption.getEstimatedTollCost());
        route.setRouteGeometry(selectedOption.getRouteGeometry());
        route.setTruckProfile(truckProfile);
        route.setUser(user);
        route.setStatus(Route.RouteStatus.CALCULATED);
        route.setOptimizationType(selectedOption.getOptimizationType());
        route.setRestrictionsCount(selectedOption.getRestrictionsCount());
        route.setBypassesUsed(selectedOption.getBypassesUsed());
        route.setTrafficLevel(selectedOption.getTrafficLevel());
        
        return routeRepository.save(route);
    }
    
    /**
     * Calculate average speed considering truck type and restrictions
     */
    private int calculateAverageSpeed(TruckProfile truckProfile, List<RoadRestriction> restrictions) {
        int baseSpeed = defaultSpeed;
        
        // Adjust speed based on truck type
        switch (truckProfile.getTruckType()) {
            case MINI_TRUCK -> baseSpeed = 65;
            case LIGHT_TRUCK -> baseSpeed = 60;
            case MEDIUM_TRUCK -> baseSpeed = 55;
            case HEAVY_TRUCK -> baseSpeed = 50;
            case MULTI_AXLE, TRAILER -> baseSpeed = 45;
            default -> baseSpeed = 50;
        }
        
        // Reduce speed if there are many restrictions
        if (restrictions.size() > 5) {
            baseSpeed -= 10;
        } else if (restrictions.size() > 2) {
            baseSpeed -= 5;
        }
        
        return Math.max(baseSpeed, 30); // Minimum speed
    }
    
    /**
     * Calculate estimated fuel cost
     */
    private BigDecimal calculateFuelCost(double distanceKm, TruckProfile truckProfile) {
        // Fuel efficiency based on truck type (km per liter)
        double fuelEfficiency;
        switch (truckProfile.getTruckType()) {
            case MINI_TRUCK -> fuelEfficiency = 15.0;
            case LIGHT_TRUCK -> fuelEfficiency = 12.0;
            case MEDIUM_TRUCK -> fuelEfficiency = 8.0;
            case HEAVY_TRUCK -> fuelEfficiency = 6.0;
            case MULTI_AXLE, TRAILER -> fuelEfficiency = 4.0;
            default -> fuelEfficiency = 8.0;
        }
        
        // Diesel price in India (approximate)
        double dieselPricePerLiter = 95.0; // INR
        
        double fuelNeeded = distanceKm / fuelEfficiency;
        double totalFuelCost = fuelNeeded * dieselPricePerLiter;
        
        return BigDecimal.valueOf(totalFuelCost).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    
    /**
     * Calculate estimated toll cost
     */
    private BigDecimal calculateTollCost(double distanceKm) {
        // Approximate toll cost in India (INR per km for trucks)
        double tollPerKm = 2.5; // Average toll rate
        double totalTollCost = distanceKm * tollPerKm;
        
        return BigDecimal.valueOf(totalTollCost).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    
    /**
     * Calculate Haversine distance between two points
     */
    private double calculateHaversineDistance(BigDecimal lat1, BigDecimal lon1, BigDecimal lat2, BigDecimal lon2) {
        final int R = 6371; // Radius of the earth in km
        
        double latDistance = Math.toRadians(lat2.subtract(lat1).doubleValue());
        double lonDistance = Math.toRadians(lon2.subtract(lon1).doubleValue());
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1.doubleValue())) * Math.cos(Math.toRadians(lat2.doubleValue()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c; // Distance in km
    }
    
    /**
     * Check if current time falls within restriction period
     */
    private boolean isTimeInRestrictionPeriod(LocalTime currentTime, LocalTime startTime, LocalTime endTime) {
        if (startTime.isBefore(endTime)) {
            // Same day restriction
            return !currentTime.isBefore(startTime) && !currentTime.isAfter(endTime);
        } else {
            // Overnight restriction
            return !currentTime.isBefore(startTime) || !currentTime.isAfter(endTime);
        }
    }
    
    /**
     * Generate sample route geometry (in real implementation, get from routing API)
     */
    private String generateSampleRouteGeometry(RouteRequestDto routeRequest) {
        // This is a simplified example - in real implementation, this would come from the routing API
        return String.format("LINESTRING(%s %s, %s %s)", 
                routeRequest.getStartLongitude(), routeRequest.getStartLatitude(),
                routeRequest.getEndLongitude(), routeRequest.getEndLatitude());
    }
    
    /**
     * Validate route request parameters
     */
    private void validateRouteRequest(RouteRequestDto routeRequest) {
        if (routeRequest.getStartLatitude() == null || routeRequest.getStartLongitude() == null ||
            routeRequest.getEndLatitude() == null || routeRequest.getEndLongitude() == null) {
            throw new RuntimeException("Start and end coordinates are required");
        }
        
        if (routeRequest.getTruckProfileId() == null) {
            throw new RuntimeException("Truck profile ID is required");
        }
        
        // Check if the route distance is within limits
        double distance = calculateHaversineDistance(
                routeRequest.getStartLatitude(), routeRequest.getStartLongitude(),
                routeRequest.getEndLatitude(), routeRequest.getEndLongitude()
        );
        
        if (distance > maxRouteDistance) {
            throw new RuntimeException("Route distance exceeds maximum limit of " + maxRouteDistance + " km");
        }
    }
}
