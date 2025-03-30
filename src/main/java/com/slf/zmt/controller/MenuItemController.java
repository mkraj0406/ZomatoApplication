package com.slf.zmt.controller;

import com.slf.zmt.entity.MenuItem;
import com.slf.zmt.exception.MenuItemNotFoundException;
import com.slf.zmt.mapper.MenuItemMapper;
import com.slf.zmt.requestdto.MenuItemRequestDto;
import com.slf.zmt.responsedto.MenuItemResponseDto;
import com.slf.zmt.service.MenuItemService;
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
import java.util.Optional;

@RestController
//@RequestMapping("/api/menu")
public class MenuItemController {

    private static final Logger log = LogManager.getLogger(MenuItemController.class);
    private final MenuItemService menuItemService;
    private final ResponseStructureBuilder responseStructureBuilder;
    private final MenuItemMapper menuItemMapper;

    public MenuItemController(MenuItemService menuItemService, ResponseStructureBuilder responseStructureBuilder, MenuItemMapper menuItemMapper) {
        this.menuItemService = menuItemService;
        this.responseStructureBuilder = responseStructureBuilder;
        this.menuItemMapper = menuItemMapper;
    }

    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    @PostMapping("/menuItems/{restaurantId}")
    public ResponseEntity<ResponseStructure<MenuItemResponseDto>> addMenuItem(@RequestBody MenuItem menuItem, @PathVariable Long restaurantId){
       menuItem =  menuItemService.addMenuItem(menuItem, restaurantId);
       return responseStructureBuilder.succesResponse(HttpStatus.CREATED,"menuItems added successfully",menuItemMapper.mapMenuItemToResponse(menuItem));
    }

    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANT_OWNER','CUSTOMER')")
    @GetMapping("/menuItems/{restaurantId}")
    public ResponseEntity<ResponseStructure<List<MenuItemResponseDto>>> getMenuByRestaurantId(@PathVariable Long restaurantId){
        List<MenuItem>  menuItems = menuItemService.getMenuByRestaurantId(restaurantId);
        List<MenuItemResponseDto> menuItemResponseDtos = new ArrayList<MenuItemResponseDto>();
        for(MenuItem menuItem : menuItems){
            menuItemResponseDtos.add(menuItemMapper.mapMenuItemToResponse(menuItem));
        }
        return responseStructureBuilder.succesResponse(HttpStatus.FOUND,"MenuItems Details successfully fetched",menuItemResponseDtos);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANT_OWNER','CUSTOMER')")
    @GetMapping("/menuItems/search")
    public ResponseEntity<ResponseStructure<List<MenuItemResponseDto>>> getMenuByName(@RequestParam String name){
        log.info(name);
        List<MenuItem>  menuItems = menuItemService.getMenuByName(name);
        List<MenuItemResponseDto> menuItemResponseDtos = new ArrayList<MenuItemResponseDto>();
        for(MenuItem menuItem : menuItems){
            menuItemResponseDtos.add(menuItemMapper.mapMenuItemToResponse(menuItem));
        }
        return responseStructureBuilder.succesResponse(HttpStatus.FOUND,"MenuItems Details successfully fetched",menuItemResponseDtos);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANT_OWNER')")
    @PutMapping("/menuItems/{menuId}")
    public ResponseEntity<ResponseStructure<MenuItemResponseDto>> updateMenuItem(@RequestBody MenuItemRequestDto menuItemRequestDto, @PathVariable Long menuId){
        MenuItem menuItem = menuItemService.updateMenuItem(menuItemRequestDto,menuId);
        return responseStructureBuilder.succesResponse(HttpStatus.OK,"MenuItem Updated successfully",menuItemMapper.mapMenuItemToResponse(menuItem));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANT_OWNER')")
    @DeleteMapping("/menuItems/{menuId}")
    public ResponseEntity<ResponseStructure<Boolean>>  deleteMenuItem(@PathVariable Long menuId){
      Boolean menuItemStatus = menuItemService.deleteMenuItem(menuId);
       if(menuItemStatus){
           return responseStructureBuilder.succesResponse(HttpStatus.OK,"MenuItem Successfully Deleted!!",menuItemStatus);
       }
       throw new MenuItemNotFoundException("no menuItem present for given ID");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANT_OWNER','CUSTOMER')")
    @GetMapping("/menuItems")
    public ResponseEntity<ResponseStructure<List<MenuItemResponseDto>>> getAllMenuDetail(){
       List<MenuItem> menuItems = menuItemService.getAllMenuItem();
       List<MenuItemResponseDto> menuItemResponseDtos = new ArrayList<MenuItemResponseDto>();
       for(MenuItem menuItem : menuItems){
           menuItemResponseDtos.add(menuItemMapper.mapMenuItemToResponse(menuItem));
       }
       return responseStructureBuilder.succesResponse(HttpStatus.FOUND,"List of MenuItem are successfully fetched!!",menuItemResponseDtos);
    }
}
