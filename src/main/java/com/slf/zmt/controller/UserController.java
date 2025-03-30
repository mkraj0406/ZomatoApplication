package com.slf.zmt.controller;

import com.slf.zmt.entity.User;
import com.slf.zmt.mapper.UserMapper;
import com.slf.zmt.requestdto.LoginRequest;
import com.slf.zmt.requestdto.UserRequestDto;
import com.slf.zmt.responsedto.UserResponseDto;
import com.slf.zmt.security.JwtUtil;
import com.slf.zmt.service.UserService;
import com.slf.zmt.utils.ResponseStructure;
import com.slf.zmt.utils.ResponseStructureBuilder;
import jakarta.persistence.Enumerated;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;
import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final ResponseStructureBuilder responseStructureBuilder;
    private final UserMapper userMapper;

    public UserController(UserService userService, JwtUtil jwtUtil, ResponseStructureBuilder responseStructureBuilder, UserMapper userMapper) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.responseStructureBuilder = responseStructureBuilder;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRequestDto userRequestDto){
        return  userService.registerUser(userRequestDto);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ResponseStructure<UserResponseDto>> verifyOtp(@RequestBody UserRequestDto userRequestDto, @RequestParam String email, @RequestParam String otp){
        return userService.verifyOtp(userRequestDto,email, otp);
    }


    @PostMapping("/login")
    public ResponseEntity<ResponseStructure<String>> loginUser(@RequestBody LoginRequest loginRequest,@RequestParam String role) throws RoleNotFoundException, LoginException {
        String token=null;
        Optional<User> optionalUser = userService.loginUser(loginRequest, role);
        if (optionalUser.isPresent()) {
           token = jwtUtil.generateToken(loginRequest.getEmail(), String.valueOf(role));
            System.out.println("token= " + token+ " Role:"+role);
        }
        return responseStructureBuilder.succesResponse(HttpStatus.OK, "Login Successfully", "Bearer " + token);

    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/")
    public  ResponseEntity<ResponseStructure<List<UserResponseDto>>> findAllUser(){
       List<User> users = userService.findAllUser();
       List<UserResponseDto> userResponseDtos = new ArrayList<UserResponseDto>();
       for(User user : users){
          userResponseDtos.add(userMapper.mapUserToResponse(user));
       }
       return responseStructureBuilder.succesResponse(HttpStatus.FOUND,"User Details successfully Fetched",userResponseDtos);
    }
}
