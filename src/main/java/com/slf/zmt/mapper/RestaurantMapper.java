package com.slf.zmt.mapper;

import com.slf.zmt.entity.Restaurant;
import com.slf.zmt.entity.User;
import com.slf.zmt.requestdto.RestaurantRequestDto;
import com.slf.zmt.responsedto.RestaurantResponseDto;
import org.springframework.stereotype.Component;

@Component
public class RestaurantMapper {

    public Restaurant mapRestaurantToEntity(RestaurantRequestDto restaurantRequestDto, User user, Restaurant restaurant){
        restaurant.setName(restaurantRequestDto.getName());
        restaurant.setLocation(restaurantRequestDto.getLocation());
        restaurant.setCuisineType(restaurantRequestDto.getCuisineType());
        restaurant.setPhoneNo(restaurantRequestDto.getPhoneNo());
        restaurant.setRating(restaurantRequestDto.getRating());
        restaurant.setClosingTime(restaurantRequestDto.getClosingTime());
        restaurant.setOpeningTime(restaurantRequestDto.getOpeningTime());
        restaurant.setUser(user);

        return restaurant;
    }

    public RestaurantResponseDto mapRestaurantToResponse(Restaurant restaurant){
        RestaurantResponseDto restaurantResponseDto = new RestaurantResponseDto();
        restaurantResponseDto.setRestaurantId(restaurant.getRestaurantId());
        restaurantResponseDto.setName(restaurant.getName());
        restaurantResponseDto.setPhoneNo(restaurant.getPhoneNo());
        restaurantResponseDto.setLocation(restaurant.getLocation());
        restaurantResponseDto.setCuisineType(restaurant.getCuisineType());
        restaurantResponseDto.setRating(restaurant.getRating());
        restaurantResponseDto.setOpeningTime(restaurant.getOpeningTime());
        restaurantResponseDto.setClosingTime(restaurant.getClosingTime());
        restaurantResponseDto.setUserId(restaurant.getUser().getUserId());

        return restaurantResponseDto;
    }
}
