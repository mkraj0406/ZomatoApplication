package com.slf.zmt.service;


import com.slf.zmt.entity.Order;
import com.slf.zmt.entity.OrderItem;
import com.slf.zmt.entity.User;
import com.slf.zmt.exception.OrderItemNotFoundException;
import com.slf.zmt.exception.OrderNotFoundException;
import com.slf.zmt.mapper.OrderMapper;
import com.slf.zmt.repository.OrderItemRepository;
import com.slf.zmt.repository.OrderRepository;
import com.slf.zmt.repository.UserRepository;
import com.slf.zmt.responsedto.OrderResponseDto;
import com.slf.zmt.utils.ResponseStructure;
import com.slf.zmt.utils.ResponseStructureBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

//    private final OrderItem orderItem;

    private final OrderItemRepository orderItemRepository;
    private final ResponseStructureBuilder responseStructureBuilder;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, OrderItemRepository orderItemRepository, ResponseStructureBuilder responseStructureBuilder, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
        this.responseStructureBuilder = responseStructureBuilder;
        this.orderMapper = orderMapper;
    }

    public ResponseEntity<ResponseStructure<OrderResponseDto>> createOrder(List<Long> orderItemIds, Long userId) {
        User user = null;
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            List<OrderItem> orderItems = orderItemRepository.findAllByUserId(user.getUserId());
            List<OrderItem> orderOrderItems = new ArrayList<>();
            if(orderItems.isEmpty()){
               throw new OrderItemNotFoundException("order item is empty!!");
            }else{
                for (OrderItem orderItem1 : orderItems) {
                    for (Long orderItemsId : orderItemIds) {
                        if (Objects.equals(orderItem1.getOrderItemId(), orderItemsId) ) {
                            if(orderItem1.getOrder() != null){
                                throw new OrderItemNotFoundException("Order item is already ordered with respected id!");
                            }
                            orderOrderItems.add(orderItem1);
                        }
                    }
                }
            }

            Order order = new Order();
            order.setUser(user);
            order.setItems(orderOrderItems);
            order.setRestaurant(orderOrderItems.get(0).getMenuItem().getRestaurant());
            order.setStatus(Order.OrderStatus.CONFIRMED);
            order = orderRepository.save(order);

            // Step 2: Set the order in each order item and save them
            for (OrderItem orderItem : orderOrderItems) {
                orderItem.setOrder(order);
            }
            orderItemRepository.saveAll(orderOrderItems); // persist updated references
            OrderResponseDto orderResponseDto = orderMapper.mapOrderToResponse(order);
            return responseStructureBuilder.succesResponse(HttpStatus.CREATED, "Order successfully placed!!", orderResponseDto);

        }else{
            throw new UsernameNotFoundException("user id not found!!");
        }

    }


    public ResponseEntity<ResponseStructure<OrderResponseDto>> updateOrderStatus(Order.OrderStatus orderStatus, Long orderId, Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()){
           Optional<Order> optionalOrder = orderRepository.findById(orderId);
           if(optionalOrder.isPresent()){
             Order order = optionalOrder.get();
             order.setStatus(orderStatus);

             order = orderRepository.save(order);
             return responseStructureBuilder.succesResponse(HttpStatus.OK,"Status updated successfully!!",orderMapper.mapOrderToResponse(order));
           }else {
               throw new OrderNotFoundException("Order not present in database!!");
           }

        }else{
            throw new UsernameNotFoundException("User Id not Found!!");
        }

    }

    public List<Order> getAllOrderDetails(Long userId) {
      Optional<User> optionalUser = userRepository.findById(userId);

      if(optionalUser.isPresent()){
        List<Order> orders =  orderRepository.findOrdersByUserId(optionalUser.get().getUserId());
        if(orders.isEmpty()){
            throw new OrderNotFoundException("No order details is present in database");
        }else {
            return orders;
        }
      }else {
          throw new UsernameNotFoundException("failed to find the user id!!");
      }
    }

    public ResponseEntity<ResponseStructure<OrderResponseDto>> getOrderByIdAndEmail(Long orderId, String email) {
        Optional<User> optionalUser= userRepository.findByEmail(email);
        if(optionalUser.isPresent()){
            Optional<Order> optionalOrder = orderRepository.findByOrderIdAndUser_UserId(orderId,optionalUser.get().getUserId());
            if(optionalOrder.isPresent()){
                OrderResponseDto orderResponseDto = orderMapper.mapOrderToResponse(optionalOrder.get());
                return responseStructureBuilder.succesResponse(HttpStatus.OK,"order successfully fetched based on id and email!!",orderResponseDto);
            }else {
                throw new OrderNotFoundException("Failed to fetch the order based on Id!");
            }
        }else {
            throw new UsernameNotFoundException("user id is wrong");
        }
    }
}
