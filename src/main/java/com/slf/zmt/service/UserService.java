package com.slf.zmt.service;


import com.slf.zmt.entity.User;
import com.slf.zmt.mapper.UserMapper;
import com.slf.zmt.repository.UserRepository;
import com.slf.zmt.requestdto.LoginRequest;
import com.slf.zmt.requestdto.UserRequestDto;
import com.slf.zmt.responsedto.UserResponseDto;
import com.slf.zmt.utils.ResponseStructure;
import com.slf.zmt.utils.ResponseStructureBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.RoleNotFoundException;
import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    private final  UserRepository userRepository;
    private final EmailService emailService;
//    private PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private  final ResponseStructureBuilder responseStructureBuilder;

    private final   ConcurrentHashMap<String,String> otpCacheStore = new ConcurrentHashMap<>();

    public UserService(UserRepository userRepository, EmailService emailService, UserMapper userMapper, ResponseStructureBuilder responseStructureBuilder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.userMapper = userMapper;
        this.responseStructureBuilder = responseStructureBuilder;
    }

    public ResponseEntity<String> registerUser(UserRequestDto userRequestDto){
           Optional<User> oldUser =  userRepository.findByEmail(userRequestDto.getEmail());
           if(oldUser.isPresent()){
               throw new RuntimeException("Email is already registered!");
           }
        String otp = generateOtp();
        otpCacheStore.put(userRequestDto.getEmail(),otp);
        emailService.sendOtpToEmail(userRequestDto.getEmail(),otp);
        return ResponseEntity.status(HttpStatus.OK).body("Otp is send to your email, Please Verify");
    }

    private String generateOtp(){
        return String.format("%06d", new Random().nextInt(1000000));
    }

    public  ResponseEntity<ResponseStructure<UserResponseDto>> verifyOtp(UserRequestDto userRequestDto, String email, String otp){
        User user=null;
        String storedOtp = otpCacheStore.get(email);
        if(storedOtp!=null && storedOtp.equals(otp)){
            otpCacheStore.remove(email);
            try{
                user =  userMapper.mapUserToEntity(userRequestDto,new User());
                user= saveUserAfterOtpVerification(user);
            }catch(Exception e){
                e.getMessage();
            }
            return responseStructureBuilder.succesResponse(HttpStatus.ACCEPTED,"otp-verified succesfull", userMapper.mapUserToResponse(user));
        }else{
            return responseStructureBuilder.succesResponse(HttpStatus.NOT_ACCEPTABLE,"otp-varification failed",userMapper.mapUserToResponse(user));
        }
    }

    @Transactional
    public User saveUserAfterOtpVerification(User user) {
        try{
//            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user = userRepository.save(user);
        } catch (Exception e) {
           e.getMessage();
        }
        return user;
    }


    public Optional<User> loginUser(LoginRequest loginRequest, String role) throws RoleNotFoundException, LoginException {
        Optional<User>  optionalUser=userRepository.findByEmail(loginRequest.getEmail());
//        if (optionalUser != null && passwordEncoder.matches(loginRequest.getPassword(), optionalUser.get().getPassword())) {
//            return optionalUser;
//        }


        if (optionalUser.isPresent() && optionalUser.get().getPassword().equals(loginRequest.getPassword())) {
            User.Role userRole = optionalUser.get().getRole();
            String stringRole=String.valueOf(userRole);
            if(stringRole.equals(role)){
                return optionalUser;
            }else {
                throw new RoleNotFoundException("unauthorized role");
            }
        }
       throw new LoginException("invalid credentials");
    }


    public List<User> findAllUser() {
        List<User> users = userRepository.findAll();
        if(users.isEmpty()){
            throw new UsernameNotFoundException("no users available in database");
        }
        return users;
    }
}
