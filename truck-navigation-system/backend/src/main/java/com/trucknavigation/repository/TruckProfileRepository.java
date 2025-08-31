package com.trucknavigation.repository;

import com.trucknavigation.model.TruckProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TruckProfileRepository extends JpaRepository<TruckProfile, Long> {
    
    List<TruckProfile> findByUserIdAndIsActiveTrue(Long userId);
    
    List<TruckProfile> findByUserId(Long userId);
    
    Optional<TruckProfile> findByIdAndUserId(Long id, Long userId);
    
    Optional<TruckProfile> findByNameAndUserId(String name, Long userId);
    
    List<TruckProfile> findByTruckTypeAndIsActiveTrue(TruckProfile.TruckType truckType);
    
    @Query("SELECT tp FROM TruckProfile tp WHERE tp.user.id = :userId AND tp.isActive = true ORDER BY tp.updatedAt DESC")
    List<TruckProfile> findActiveProfilesByUserOrderByUpdatedDesc(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(tp) FROM TruckProfile tp WHERE tp.user.id = :userId AND tp.isActive = true")
    long countActiveProfilesByUser(@Param("userId") Long userId);
    
    boolean existsByNameAndUserIdAndIsActiveTrue(String name, Long userId);
}
