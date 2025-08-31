package com.trucknavigation.repository;

import com.trucknavigation.model.RoadRestriction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RoadRestrictionRepository extends JpaRepository<RoadRestriction, Long> {
    
    @Query("SELECT rr FROM RoadRestriction rr WHERE " +
           "rr.latitude >= :minLat AND rr.latitude <= :maxLat AND " +
           "rr.longitude >= :minLon AND rr.longitude <= :maxLon AND " +
           "rr.isActive = true")
    List<RoadRestriction> findRestrictionsInArea(
            @Param("minLat") BigDecimal minLat, 
            @Param("maxLat") BigDecimal maxLat,
            @Param("minLon") BigDecimal minLon, 
            @Param("maxLon") BigDecimal maxLon);
    
    List<RoadRestriction> findByRestrictionTypeAndIsActiveTrue(RoadRestriction.RestrictionType restrictionType);
    
    List<RoadRestriction> findByStateAndIsActiveTrue(String state);
    
    List<RoadRestriction> findByCityAndIsActiveTrue(String city);
    
    List<RoadRestriction> findByHighwayAndIsActiveTrue(String highway);
    
    @Query("SELECT rr FROM RoadRestriction rr WHERE " +
           "rr.maxHeight < :height AND rr.isActive = true")
    List<RoadRestriction> findHeightRestrictions(@Param("height") BigDecimal height);
    
    @Query("SELECT rr FROM RoadRestriction rr WHERE " +
           "rr.maxWeight < :weight AND rr.isActive = true")
    List<RoadRestriction> findWeightRestrictions(@Param("weight") BigDecimal weight);
    
    @Query("SELECT rr FROM RoadRestriction rr WHERE " +
           "rr.trucksProhibited = true AND rr.isActive = true")
    List<RoadRestriction> findTruckProhibitedAreas();
    
    @Query("SELECT rr FROM RoadRestriction rr WHERE " +
           "rr.hazmatProhibited = true AND rr.isActive = true")
    List<RoadRestriction> findHazmatProhibitedAreas();
}
