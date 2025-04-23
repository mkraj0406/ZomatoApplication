package com.slf.zmt.repository;

import com.slf.zmt.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {


    List<OrderItem> findByUserUserId(Long userId);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.user.userId = :userId")
    List<OrderItem> findAllByUserId(@Param("userId") Long userId);

}
