package com.slf.zmt.repository;


import com.slf.zmt.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem,Long> {

    List<MenuItem> findByNameContainingIgnoreCase(String name);

    List<MenuItem> findByPriceBetween(Double minPrice, Double maxPrice);

    //    List<MenuItem> findByRestaurantId(Long restaurantId);
    @Query("SELECT m FROM MenuItem m WHERE m.restaurant.id = :restaurantId")
    List<MenuItem> findByRestaurantId(@Param("restaurantId") Long restaurantId);


}
