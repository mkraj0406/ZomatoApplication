package com.slf.zmt.mapper;

import com.slf.zmt.entity.MenuItem;
import com.slf.zmt.requestdto.MenuItemRequestDto;
import com.slf.zmt.responsedto.MenuItemResponseDto;
import org.springframework.stereotype.Component;

@Component
public class MenuItemMapper {

    public MenuItem mapResponseToEntity(MenuItemRequestDto menuItemRequestDto,MenuItem menuItem){
        menuItem.setName(menuItemRequestDto.getName());
        menuItem.setDescription(menuItemRequestDto.getDescription());
        menuItem.setPrice(menuItemRequestDto.getPrice());

        return menuItem;
    }

    public MenuItemResponseDto mapMenuItemToResponse(MenuItem menuItem){
        MenuItemResponseDto menuItemResponseDto = new MenuItemResponseDto();
        menuItemResponseDto.setMenuId(menuItem.getMenuId());
        menuItemResponseDto.setName(menuItem.getName());
        menuItemResponseDto.setPrice(menuItem.getPrice());
        menuItemResponseDto.setDescription(menuItem.getDescription());
        menuItemResponseDto.setRestaurantId(menuItem.getRestaurant().getRestaurantId());

        return menuItemResponseDto;
    }
}
