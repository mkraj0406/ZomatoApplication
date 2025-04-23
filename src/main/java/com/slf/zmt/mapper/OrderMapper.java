package com.slf.zmt.mapper;

import com.slf.zmt.entity.Order;
import com.slf.zmt.responsedto.OrderResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class OrderMapper {

    public OrderResponseDto mapOrderToResponse(Order order){
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setOrderId(order.getOrderId());
        orderResponseDto.setOrderDate(order.getOrderDate());
        orderResponseDto.setCreatedAt(order.getCreatedAt());
        orderResponseDto.setRestaurantId(order.getRestaurant().getRestaurantId());
        List<Long> orderItemIds= new ArrayList<>();
        for(int i=0;i<order.getItems().size();i++){
            orderItemIds.add(order.getItems().get(i).getOrderItemId());
        }
        orderResponseDto.setItemsIds(orderItemIds);
        orderResponseDto.setStatus(order.getStatus());
        orderResponseDto.setUpdatedAt(order.getUpdatedAt());
        orderResponseDto.setStatusUpdatedAt(order.getStatusUpdatedAt());
        orderResponseDto.setTotalAmount(order.getTotalAmount());
        orderResponseDto.setUserId(order.getUser().getUserId());

        return orderResponseDto;
    }
}
