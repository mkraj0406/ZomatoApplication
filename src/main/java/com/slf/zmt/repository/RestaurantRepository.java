package com.slf.zmt.repository;

import com.slf.zmt.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {

    @Query("SELECT r FROM Restaurant r WHERE r.user.id = :userId")
    List<Restaurant> findRestaurantsByUserId(@Param("userId") Long userId);
    // Exact match with phone number
    Optional<Restaurant> findByPhoneNo(String phoneNo);

    // Location (case-insensitive search)
    List<Restaurant> findByLocationIgnoreCase(String location);

    // Cuisine Type (case-insensitive search)
    List<Restaurant> findByCuisineTypeIgnoreCase(String cuisineType);

    // Partial Name Match (e.g., %Pizza%)
    List<Restaurant> findByNameContainingIgnoreCase(String name);

    // Custom query: Filter by location and cuisine type
    @Query("SELECT r FROM Restaurant r WHERE r.location = :location AND r.cuisineType = :cuisineType")
    List<Restaurant> findByLocationAndCuisineType(@Param("locations") String location, @Param("cuisine_type") String cuisineType);

    // Filter by rating range
    List<Restaurant> findByRatingBetween(Double minRating, Double maxRating);

    List<Restaurant> findByLocationAndCuisineTypeIgnoreCase(String location, String cuisineType);
}
