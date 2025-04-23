package com.slf.zmt.controller;

import com.slf.zmt.entity.OrderItem;
import com.slf.zmt.exception.OrderItemNotFoundException;
import com.slf.zmt.mapper.OrderItemMapper;
import com.slf.zmt.responsedto.OrderItemResponseDto;
import com.slf.zmt.service.OrderItemService;
import com.slf.zmt.utils.ResponseStructure;
import com.slf.zmt.utils.ResponseStructureBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orderItems")
public class OrderItemController {

    private static final Logger log = LogManager.getLogger(OrderItemController.class);
    private final OrderItemService orderItemService;
    private final ResponseStructureBuilder responseStructureBuilder;

    private final OrderItemMapper orderItemMapper;

    public OrderItemController(OrderItemService orderItemService, ResponseStructureBuilder responseStructureBuilder, OrderItemMapper orderItemMapper) {
        this.orderItemService = orderItemService;
        this.responseStructureBuilder = responseStructureBuilder;
        this.orderItemMapper = orderItemMapper;
    }

    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANT_OWNER','CUSTOMER')")
    @PostMapping("/menuItems/{menuId}/users/{userId}")
    public ResponseEntity<ResponseStructure<OrderItemResponseDto>> addOrderItem(@PathVariable Long menuId, @PathVariable Long userId){
        log.info("adding your items into cart");
         return orderItemService.addOrderItem(menuId,userId);
    }


    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANT_OWNER','CUSTOMER')")
    @PutMapping("/{orderItemId}/users/{userId}")
    public ResponseEntity<ResponseStructure<OrderItemResponseDto>> updateOrderItem(@RequestParam int quantity,@PathVariable Long orderItemId,@PathVariable Long userId){
        return orderItemService.updateOrderItem(quantity,orderItemId,userId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANT_OWNER','CUSTOMER')")
    @GetMapping("/users/{userId}")
   public ResponseEntity<ResponseStructure<List<OrderItemResponseDto>>> getAllOrderItemByUserId(@PathVariable Long userId){
       List<OrderItem> orderItems = orderItemService.getAllOrderItemByUserId(userId);
       List<OrderItemResponseDto> orderItemResponseDtos = new ArrayList<>();
       for(OrderItem orderItem : orderItems){
         OrderItemResponseDto orderItemResponseDto  = orderItemMapper.mapOrderItemToResponse(orderItem);
           orderItemResponseDtos.add(orderItemResponseDto);
       }
       return responseStructureBuilder.succesResponse(HttpStatus.FOUND,"List of all orders",orderItemResponseDtos);
   }

    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANT_OWNER','CUSTOMER')")
    @DeleteMapping("/{orderItemId}/users/{userId}")
    public ResponseEntity<String> deleteOrderItemById(@PathVariable Long orderItemId,@PathVariable Long userId){
       Boolean status = orderItemService.deleteOrderItemById(orderItemId,userId);
       if(status) {
           return ResponseEntity.status(HttpStatus.OK).body("successfully order Item is deleted");
       }else{
           throw new OrderItemNotFoundException("No order item found in database");
       }
    }

}
