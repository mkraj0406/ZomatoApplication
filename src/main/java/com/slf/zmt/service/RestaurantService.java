package com.slf.zmt.service;

import ch.qos.logback.classic.spi.IThrowableProxy;
import com.slf.zmt.entity.Restaurant;
import com.slf.zmt.entity.User;
import com.slf.zmt.exception.RestaurantNotFoudException;
import com.slf.zmt.exception.UserPhoneNoAlreadyExistException;
import com.slf.zmt.mapper.RestaurantMapper;
import com.slf.zmt.repository.RestaurantRepository;
import com.slf.zmt.repository.UserRepository;
import com.slf.zmt.requestdto.RestaurantRequestDto;
import com.slf.zmt.utils.ResponseStructure;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    private final RestaurantMapper restaurantMapper;

    private final UserRepository userRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, RestaurantMapper restaurantMapper, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantMapper = restaurantMapper;
        this.userRepository = userRepository;
    }

    public Restaurant createRestaurant(RestaurantRequestDto restaurantRequestDto, Long userId) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findByPhoneNo(restaurantRequestDto.getPhoneNo());
        if (optionalRestaurant.isPresent()) {
            throw new UserPhoneNoAlreadyExistException("User phone number already exist");
        }
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            Restaurant restaurant = restaurantMapper.mapRestaurantToEntity(restaurantRequestDto, userOptional.get(), new Restaurant());
            return restaurantRepository.save(restaurant);
        }
        return null;
    }

    public List<Restaurant> getByLocation(String location) {
        List<Restaurant> restaurants= restaurantRepository.findByLocationIgnoreCase(location);
        if (restaurants.isEmpty()) {
            throw new RestaurantNotFoudException("No restaurant at this place");
        }
        return restaurants;
    }

    public List<Restaurant> getByCuisineType(String cuisineType) {
        List<Restaurant> restaurants = restaurantRepository.findByCuisineTypeIgnoreCase(cuisineType);
        if (restaurants.isEmpty()) {
            throw new RestaurantNotFoudException("No restaurant have that cuisine");
        }
        return restaurants;
    }

    public List<Restaurant> searchByName(String name) {
        List<Restaurant> restaurants= restaurantRepository.findByNameContainingIgnoreCase(name);
        if (restaurants.isEmpty()) {
            throw new RestaurantNotFoudException("No restaurant have found by name");
        }
        return restaurants;
    }

    public List<Restaurant> getByLocationAndCuisineType(String location, String cuisineType) {
        List<Restaurant> restaurants = restaurantRepository.findByLocationAndCuisineTypeIgnoreCase(location, cuisineType);
        if (restaurants.isEmpty()) {
            throw new RestaurantNotFoudException("No restaurant found by cuisine and location");
        }
        return restaurants;
    }

    public List<Restaurant> getByRatingRange(Double min, Double max) {
        List<Restaurant> restaurants =  restaurantRepository.findByRatingBetween(min, max);
        if (restaurants.isEmpty()) {
            throw new RestaurantNotFoudException("No restaurant got this rating");
        }
        return restaurants;
    }

    public Restaurant updateRestaurant(RestaurantRequestDto restaurantRequestDto, Long restaurantId) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        if (optionalRestaurant.isPresent()) {
            Restaurant restaurant = optionalRestaurant.get();
            restaurant.setName(restaurantRequestDto.getName());
            restaurant.setLocation(restaurantRequestDto.getLocation());
            restaurant.setPhoneNo(restaurantRequestDto.getPhoneNo());
            restaurant.setCuisineType(restaurantRequestDto.getCuisineType());
            restaurant.setRating(restaurantRequestDto.getRating());
            restaurant.setOpeningTime(restaurant.getOpeningTime());
            restaurant.setClosingTime(restaurantRequestDto.getClosingTime());

            return restaurantRepository.save(restaurant);

        } else {
            throw new RestaurantNotFoudException("Not Any Restaurant present!!");
        }
    }

    public Boolean deleteRestaurant(Long restaurantId) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        if (optionalRestaurant.isPresent()) {
            Restaurant restaurant = optionalRestaurant.get();
            restaurantRepository.delete(restaurant);
            return true;
        } else {
            return false;
        }
    }

    public List<Restaurant> findAllRestaurant() {
        return restaurantRepository.findAll();
    }

    public List<Restaurant> getRestaurantByUserId(Long userId) {
      List<Restaurant> restaurants = restaurantRepository.findRestaurantsByUserId(userId);
      if(restaurants.isEmpty()){
          throw new RestaurantNotFoudException("Don't have any restaurant ownership, the list is empty");
      }
      return restaurants;
    }
}
