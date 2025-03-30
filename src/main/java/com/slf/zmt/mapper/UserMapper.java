package com.slf.zmt.mapper;

import com.slf.zmt.entity.User;
import com.slf.zmt.requestdto.UserRequestDto;
import com.slf.zmt.responsedto.UserResponseDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User mapUserToEntity(UserRequestDto userRequestDto,User user){
        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(userRequestDto.getPassword());
        user.setRole(userRequestDto.getRole());

        return user;
    }


    public UserResponseDto mapUserToResponse(User user){
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUserId(user.getUserId());
        userResponseDto.setName(user.getName());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setRole(user.getRole());

       return userResponseDto;
    }




}
