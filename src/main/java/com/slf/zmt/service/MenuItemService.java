package com.slf.zmt.service;

import com.slf.zmt.entity.MenuItem;
import com.slf.zmt.entity.Restaurant;
import com.slf.zmt.exception.MenuItemNotFoundException;
import com.slf.zmt.exception.RestaurantNotFoudException;
import com.slf.zmt.repository.MenuItemRepository;
import com.slf.zmt.repository.RestaurantRepository;
import com.slf.zmt.requestdto.MenuItemRequestDto;
import com.slf.zmt.utils.ResponseStructureBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final ResponseStructureBuilder responseStructureBuilder;

    public MenuItemService(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository, ResponseStructureBuilder responseStructureBuilder) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
        this.responseStructureBuilder = responseStructureBuilder;
    }

    public MenuItem addMenuItem(MenuItem menuItem, Long restaurantId) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        if(optionalRestaurant.isPresent()){
            menuItem.setRestaurant(optionalRestaurant.get());
            return menuItemRepository.save(menuItem);
        }else{
            throw new RestaurantNotFoudException("No restaurant found with the give Id");
        }

    }

    public List<MenuItem> getMenuByRestaurantId(Long restaurantId) {
       List<MenuItem>  menuItems = menuItemRepository.findByRestaurantId(restaurantId);
       if(menuItems.isEmpty()){
           throw new MenuItemNotFoundException("there is no MenuItem present based on restaurant, or may be restaurant is not there");
       }else{
           return menuItems;
       }
    }

    public List<MenuItem> getMenuByName(String name) {
        List<MenuItem>  menuItems = menuItemRepository.findByNameContainingIgnoreCase(name);
        if(menuItems.isEmpty()){
            throw new MenuItemNotFoundException("there is no MenuItem present based on name!!");
        }else{
            return menuItems;
        }
    }

    public MenuItem updateMenuItem(MenuItemRequestDto menuItemRequestDto, Long menuId) {
       Optional<MenuItem> optionalMenuItem = menuItemRepository.findById(menuId);
       if(optionalMenuItem.isPresent()){
           MenuItem updateMenuItem = optionalMenuItem.get();
           updateMenuItem.setName(menuItemRequestDto.getName());
           updateMenuItem.setPrice(menuItemRequestDto.getPrice());
           updateMenuItem.setDescription(menuItemRequestDto.getDescription());

           return menuItemRepository.save(updateMenuItem);
       }else{
           throw new MenuItemNotFoundException("Not Menu item present in datbaase");
       }
    }

    public Boolean deleteMenuItem(Long menuItem) {
      Optional<MenuItem> optionalMenuItem= menuItemRepository.findById(menuItem);
      if(optionalMenuItem.isPresent()){
          menuItemRepository.delete(optionalMenuItem.get());
          return true;
      }
      return false;
    }

    public List<MenuItem> getAllMenuItem() {
        List<MenuItem> menuItems = menuItemRepository.findAll();
        if(menuItems.isEmpty()){
            throw new MenuItemNotFoundException("No MenuItem presents");
        }
        return menuItems;
    }
}
