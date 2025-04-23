package com.slf.zmt.mapper;


import com.slf.zmt.entity.MenuItem;
import com.slf.zmt.entity.OrderItem;
import com.slf.zmt.responsedto.OrderItemResponseDto;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {


    public OrderItemResponseDto mapOrderItemToResponse(OrderItem orderItem){
        OrderItemResponseDto orderItemResponseDto = new OrderItemResponseDto();
        orderItemResponseDto.setMenuId(orderItem.getMenuItem().getMenuId());
        orderItemResponseDto.setPrice(orderItem.getPrice());
        orderItemResponseDto.setQuantity(orderItem.getQuantity());
        orderItemResponseDto.setTotalPrice(orderItem.getTotalPrice());
        orderItemResponseDto.setUserId(orderItem.getUser().getUserId());

        return orderItemResponseDto;
    }
}
