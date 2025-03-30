package com.slf.zmt.service;

import com.slf.zmt.entity.OrderItem;
import com.slf.zmt.utils.ResponseStructure;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService {

    public ResponseEntity<ResponseStructure<OrderItem>> addOrderItem(Long menuId, OrderItemService orderItemService) {

    }
}
