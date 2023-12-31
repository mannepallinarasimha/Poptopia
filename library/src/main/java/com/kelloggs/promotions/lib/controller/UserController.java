package com.kelloggs.promotions.lib.controller;

import com.kelloggs.promotions.lib.entity.User;
import com.kelloggs.promotions.lib.model.ApiResponse;
import com.kelloggs.promotions.lib.repository.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<User> addUser(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return new ApiResponse<>("User added successfully",userRepo.save(user));
    }


}
