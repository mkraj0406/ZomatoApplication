package com.slf.zmt.controller;

import com.slf.zmt.entity.Order;
import com.slf.zmt.mapper.OrderMapper;
import com.slf.zmt.responsedto.OrderResponseDto;
import com.slf.zmt.service.OrderService;
import com.slf.zmt.utils.ResponseStructure;
import com.slf.zmt.utils.ResponseStructureBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    private final OrderMapper orderMapper;

    private final ResponseStructureBuilder responseStructureBuilder;

    public OrderController(OrderService orderService, OrderMapper orderMapper, ResponseStructureBuilder responseStructureBuilder) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
        this.responseStructureBuilder = responseStructureBuilder;
    }

    @PostMapping("/orderItems/{orderItemIds}/users/{userId}")
    public ResponseEntity<ResponseStructure<OrderResponseDto>> createOrder(@PathVariable List<Long> orderItemIds, @PathVariable Long userId) {
        return orderService.createOrder(orderItemIds, userId);
    }

    @PatchMapping("/{orderId}/users/{userId}")
    public ResponseEntity<ResponseStructure<OrderResponseDto>> updateOrderStatus(@RequestParam Order.OrderStatus orderStatus, @PathVariable Long orderId, @PathVariable Long userId) {
        return orderService.updateOrderStatus(orderStatus, orderId, userId);
    }


    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseStructure<List<OrderResponseDto>>> getAllOrderDetails(@PathVariable Long userId,Authentication authentication) {
        String email = authentication.getName();
        System.out.println(email);
        List<Order> orders = orderService.getAllOrderDetails(userId);
        List<OrderResponseDto> orderResponseDtos = new ArrayList<>();

        for (Order order : orders) {
            OrderResponseDto orderResponseDto = orderMapper.mapOrderToResponse(order);
            orderResponseDtos.add(orderResponseDto);
        }
        return responseStructureBuilder.succesResponse(HttpStatus.FOUND, "successfully fetched order", orderResponseDtos);
    }


    //    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/{orderId}")
    public ResponseEntity<ResponseStructure<OrderResponseDto>> getOrderById(@PathVariable Long orderId, Authentication authentication) {
        String email = authentication.getName();;
        return orderService.getOrderByIdAndEmail(orderId, email);
    }



}
