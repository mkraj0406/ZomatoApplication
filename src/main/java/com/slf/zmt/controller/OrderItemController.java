package com.slf.zmt.controller;

import com.slf.zmt.entity.OrderItem;
import com.slf.zmt.service.OrderItemService;
import com.slf.zmt.utils.ResponseStructure;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    public ResponseEntity<ResponseStructure<OrderItem>> addOrderItem(@RequestParam Long menuId,@RequestParam Long restaurantId){
         return orderItemService.addOrderItem(menuId,orderItemService);
    }
}
