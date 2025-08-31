package com.trucknavigation.repository;

import com.trucknavigation.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    
    List<Route> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    List<Route> findByUserIdAndIsFavoriteTrueOrderByCreatedAtDesc(Long userId);
    
    Optional<Route> findByIdAndUserId(Long id, Long userId);
    
    List<Route> findByTruckProfileIdAndUserId(Long truckProfileId, Long userId);
    
    List<Route> findByStatusAndUserId(Route.RouteStatus status, Long userId);
    
    @Query("SELECT r FROM Route r WHERE r.user.id = :userId AND r.createdAt >= :since ORDER BY r.createdAt DESC")
    List<Route> findRecentRoutesByUser(@Param("userId") Long userId, @Param("since") LocalDateTime since);
    
    @Query("SELECT COUNT(r) FROM Route r WHERE r.user.id = :userId")
    long countRoutesByUser(@Param("userId") Long userId);
    
    @Query("SELECT r FROM Route r WHERE " +
           "r.startLatitude = :startLat AND r.startLongitude = :startLon AND " +
           "r.endLatitude = :endLat AND r.endLongitude = :endLon AND " +
           "r.truckProfile.id = :truckProfileId AND " +
           "r.user.id = :userId ORDER BY r.createdAt DESC")
    List<Route> findSimilarRoutes(
            @Param("startLat") java.math.BigDecimal startLat,
            @Param("startLon") java.math.BigDecimal startLon,
            @Param("endLat") java.math.BigDecimal endLat,
            @Param("endLon") java.math.BigDecimal endLon,
            @Param("truckProfileId") Long truckProfileId,
            @Param("userId") Long userId);
}
