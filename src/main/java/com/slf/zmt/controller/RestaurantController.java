package com.slf.zmt.controller;

import com.slf.zmt.entity.Restaurant;
import com.slf.zmt.entity.User;
import com.slf.zmt.exception.RestaurantNotFoudException;
import com.slf.zmt.mapper.RestaurantMapper;
import com.slf.zmt.requestdto.RestaurantRequestDto;
import com.slf.zmt.responsedto.RestaurantResponseDto;
import com.slf.zmt.service.RestaurantService;
import com.slf.zmt.utils.ResponseStructure;
import com.slf.zmt.utils.ResponseStructureBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping("/rest")
public class RestaurantController {


    private final RestaurantService restaurantService;
    private final ResponseStructureBuilder responseStructureBuilder;
    private final RestaurantMapper restaurantMapper;

    public RestaurantController(RestaurantService restaurantService, ResponseStructureBuilder responseStructureBuilder, RestaurantMapper restaurantMapper) {
        this.restaurantService = restaurantService;
        this.responseStructureBuilder = responseStructureBuilder;
        this.restaurantMapper = restaurantMapper;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANT_OWNER')")
    @PostMapping("/restaurants/{userId}")
    public ResponseEntity<ResponseStructure<RestaurantResponseDto>> createRestaurant(@RequestBody RestaurantRequestDto restaurantRequestDto, @PathVariable Long userId) {
        Restaurant restaurant = restaurantService.createRestaurant(restaurantRequestDto, userId);
        return responseStructureBuilder.
                succesResponse(HttpStatus.CREATED,
                        "Restaurant Details Successfully Saved!",
                        restaurantMapper.mapRestaurantToResponse(restaurant));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANT_OWNER','CUSTOMER')")
    @GetMapping("/restaurants/location/{location}")
    public ResponseEntity<ResponseStructure<List<RestaurantResponseDto>>> getByLocation(@PathVariable String location) {
        List<Restaurant> restaurants = restaurantService.getByLocation(location);
        List<RestaurantResponseDto> restaurantsResponseDtosList = new ArrayList<RestaurantResponseDto>();
        for (Restaurant restaurant : restaurants) {
             RestaurantResponseDto restaurantResponseDto= restaurantMapper.mapRestaurantToResponse(restaurant);
             restaurantsResponseDtosList.add(restaurantResponseDto);
        }
        return responseStructureBuilder
                .succesResponse(HttpStatus.FOUND,
                        "Found Restaurants at " + location + " location", restaurantsResponseDtosList);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANT_OWNER','CUSTOMER')")
    @GetMapping("/restaurants/cuisine/{cuisineType}")
    public ResponseEntity<ResponseStructure<List<RestaurantResponseDto>>> getByCuisineType(@PathVariable String cuisineType) {
        List<Restaurant> restaurants = restaurantService.getByCuisineType(cuisineType);
        List<RestaurantResponseDto> restaurantResponseDtoList = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            RestaurantResponseDto restaurantResponseDto = restaurantMapper.mapRestaurantToResponse(restaurant);
            restaurantResponseDtoList.add(restaurantResponseDto);
        }
        return responseStructureBuilder.succesResponse(HttpStatus.FOUND,
                "Found Restaurants with cuisine type " + cuisineType,
                restaurantResponseDtoList);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANT_OWNER','CUSTOMER')")
    @GetMapping("/restaurants/search")
    public ResponseEntity<ResponseStructure<List<RestaurantResponseDto>>> searchByName(@RequestParam String name) {
        List<Restaurant> restaurants = restaurantService.searchByName(name);
        List<RestaurantResponseDto> restaurantResponseDtoList = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            RestaurantResponseDto restaurantResponseDto = restaurantMapper.mapRestaurantToResponse(restaurant);
            restaurantResponseDtoList.add(restaurantResponseDto);
        }
        return responseStructureBuilder.succesResponse(HttpStatus.FOUND,
                "Found Restaurants with name " + name,
                restaurantResponseDtoList);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANT_OWNER','CUSTOMER')")
    @GetMapping("/restaurants/filter")
    public ResponseEntity<ResponseStructure<List<RestaurantResponseDto>>> filterByLocationAndCuisineType(
            @RequestParam String location, @RequestParam String cuisineType) {
        List<Restaurant> restaurants = restaurantService.getByLocationAndCuisineType(location, cuisineType);
        List<RestaurantResponseDto> restaurantResponseDtoList = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            RestaurantResponseDto restaurantResponseDto = restaurantMapper.mapRestaurantToResponse(restaurant);
            restaurantResponseDtoList.add(restaurantResponseDto);
        }
        return responseStructureBuilder.succesResponse(HttpStatus.FOUND,
                "Filtered Restaurants by location and cuisine type",
                restaurantResponseDtoList);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANT_OWNER','CUSTOMER')")
    @GetMapping("/restaurants/rating")
    public ResponseEntity<ResponseStructure<List<RestaurantResponseDto>>> getByRatingRange(
            @RequestParam Double min, @RequestParam Double max) {
        List<Restaurant> restaurants = restaurantService.getByRatingRange(min, max);
        List<RestaurantResponseDto> restaurantResponseDtoList = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            RestaurantResponseDto restaurantResponseDto = restaurantMapper.mapRestaurantToResponse(restaurant);
            restaurantResponseDtoList.add(restaurantResponseDto);
        }
        return responseStructureBuilder.succesResponse(HttpStatus.FOUND,
                "Found Restaurants within rating range",
                restaurantResponseDtoList);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANT_OWNER')")
    @PutMapping("/restaurants/{restaurantId}")
    public ResponseEntity<ResponseStructure<RestaurantResponseDto>> updateRestaurant(@RequestBody RestaurantRequestDto restaurantRequestDto,@PathVariable Long restaurantId){
      Restaurant restaurant = restaurantService.updateRestaurant(restaurantRequestDto,restaurantId);
      return responseStructureBuilder.succesResponse(HttpStatus.OK,"Restaurant Updated successfully !!", restaurantMapper.mapRestaurantToResponse(restaurant));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANT_OWNER')")
    @DeleteMapping("/restaurants/{restaurantId}")
    public ResponseEntity<ResponseStructure<Boolean>> deleteRestaurant(@PathVariable Long restaurantId){
         Boolean restaurantStatus = restaurantService.deleteRestaurant(restaurantId);
         if(restaurantStatus){
             return responseStructureBuilder.succesResponse(HttpStatus.OK,"Restaurant Deleted Successfully",restaurantStatus);
         }else{
             throw new RestaurantNotFoudException("Restaurant Not Found By Id!!");
         }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @GetMapping("/restaurants")
    public ResponseEntity<ResponseStructure<List<RestaurantResponseDto>>> findAllRestaurant(){
        List<Restaurant> restaurants = restaurantService.findAllRestaurant();
        List<RestaurantResponseDto> restaurantResponseDtoList = new ArrayList<RestaurantResponseDto>();
        for (Restaurant restaurant : restaurants) {
            RestaurantResponseDto restaurantResponseDto = restaurantMapper.mapRestaurantToResponse(restaurant);
            restaurantResponseDtoList.add(restaurantResponseDto);
        }
        if(restaurants.isEmpty()){
           throw new RestaurantNotFoudException("List is empty, no restaurant present in database");
        }else{
            return responseStructureBuilder.succesResponse(HttpStatus.FOUND,"Restaurant Details Fetched Successfully!!", restaurantResponseDtoList);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANT_OWNER' )")
    @GetMapping("/restaurants/user/{userId}")
    public ResponseEntity<ResponseStructure<List<RestaurantResponseDto>>> getRestaurantByUserId(@PathVariable Long userId){
        List<Restaurant> restaurants = restaurantService.getRestaurantByUserId(userId);
        List<RestaurantResponseDto> restaurantResponseDtoList = new ArrayList<RestaurantResponseDto>();
        for(Restaurant restaurant : restaurants){
            restaurantResponseDtoList.add(restaurantMapper.mapRestaurantToResponse(restaurant));
        }
        return responseStructureBuilder.succesResponse(HttpStatus.FOUND,"List of Restaurant Based on User ID:"+userId+"!",restaurantResponseDtoList);
    }

}
