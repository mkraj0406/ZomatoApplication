package com.slf.zmt.service;

import com.slf.zmt.entity.MenuItem;
import com.slf.zmt.entity.OrderItem;
import com.slf.zmt.entity.User;
import com.slf.zmt.exception.MenuItemNotFoundException;
import com.slf.zmt.exception.OrderItemNotFoundException;
import com.slf.zmt.mapper.OrderItemMapper;
import com.slf.zmt.repository.MenuItemRepository;
import com.slf.zmt.repository.OrderItemRepository;
import com.slf.zmt.repository.UserRepository;
import com.slf.zmt.responsedto.OrderItemResponseDto;
import com.slf.zmt.utils.ResponseStructure;
import com.slf.zmt.utils.ResponseStructureBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OrderItemService {

    private final MenuItemRepository menuItemRepository;

    private final OrderItemRepository orderItemRepository;

    private final ResponseStructureBuilder responseStructureBuilder;

    private final OrderItemMapper orderItemMapper;

    private final UserRepository userRepository;

    public OrderItemService(MenuItemRepository menuItemRepository, OrderItemRepository orderItemRepository, ResponseStructureBuilder responseStructureBuilder, OrderItemMapper orderItemMapper, UserRepository userRepository) {
        this.menuItemRepository = menuItemRepository;
        this.orderItemRepository = orderItemRepository;
        this.responseStructureBuilder = responseStructureBuilder;
        this.orderItemMapper = orderItemMapper;
        this.userRepository = userRepository;
    }

    public ResponseEntity<ResponseStructure<OrderItemResponseDto>> addOrderItem(Long menuId, Long userId) {
        Optional<MenuItem> optionalMenuItem = menuItemRepository.findById(menuId);
        Optional<User> userOptional = userRepository.findById(userId);
        OrderItem orderItem = new OrderItem();

        if(userOptional.isPresent()){
            if (optionalMenuItem.isPresent()) {
                MenuItem menuItem = optionalMenuItem.get();
                orderItem.setPrice(menuItem.getPrice());
                orderItem.setQuantity(1);
                orderItem.setMenuItem(menuItem);
                orderItem.setTotalPrice(orderItem.calculateTotalPrice());
                orderItem.setUser(userOptional.get());
                orderItem = orderItemRepository.save(orderItem);

                OrderItemResponseDto orderItemResponseDto = orderItemMapper.mapOrderItemToResponse(orderItem);
                return responseStructureBuilder.succesResponse(HttpStatus.CREATED,"Order Item is created",orderItemResponseDto);
            } else {
                throw new MenuItemNotFoundException("menu item not present in database");
            }
        }else{
         throw new UsernameNotFoundException("User id is invalid");
        }
    }


    public ResponseEntity<ResponseStructure<OrderItemResponseDto>> updateOrderItem(int quantity,Long orderItemId,Long userId) {
        Optional<OrderItem> optionalOrderItem = orderItemRepository.findById(orderItemId);
      Optional<User> optionalUser = userRepository.findById(userId);
      if(optionalUser.isPresent()){
          if(optionalOrderItem.isPresent() && Objects.equals(optionalUser.get().getUserId(), optionalOrderItem.get().getUser().getUserId())){
              OrderItem orderItem = optionalOrderItem.get();
              orderItem.setQuantity(quantity);
              orderItem.setTotalPrice(orderItem.calculateTotalPrice());

              orderItem = orderItemRepository.save(orderItem);
              OrderItemResponseDto orderItemResponseDto = orderItemMapper.mapOrderItemToResponse(orderItem);
              return responseStructureBuilder.succesResponse(HttpStatus.OK,"order Item updated succesfully",orderItemResponseDto);
          }else{
              throw new OrderItemNotFoundException("No Item is added to cart yet");
          }
      }else {
          throw new UsernameNotFoundException("user is not present");
      }
    }

    public Boolean deleteOrderItemById(Long orderItemId,Long userId){
       Optional<OrderItem> optionalOrderItem = orderItemRepository.findById(orderItemId);
        if(optionalOrderItem.isPresent() && optionalOrderItem.get().getUser().getUserId().equals(userId)) {
            orderItemRepository.delete(optionalOrderItem.get());
            return true;
        } else {
            throw new OrderItemNotFoundException("Order item not found for this user");
        }
    }

    public List<OrderItem> getAllOrderItemByUserId(Long userId) {
         Optional<User> userOptional= userRepository.findById(userId);
         if(userOptional.isPresent()){
            User user = userOptional.get();
            List<OrderItem> orderItems = orderItemRepository.findAllByUserId(user.getUserId());
            return orderItems;
         }else{
          throw new UsernameNotFoundException("User is not present from this Id!!");
         }
    }
}
