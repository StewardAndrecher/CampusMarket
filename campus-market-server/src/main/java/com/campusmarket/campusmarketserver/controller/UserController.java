
package com.campusmarket.campusmarketserver.controller;

import com.campusmarket.common.Result;
import com.campusmarket.campusmarketserver.dto.LoginRequest;
import com.campusmarket.campusmarketserver.dto.LoginResponse;
import com.campusmarket.campusmarketserver.dto.RegisterRequest;
import com.campusmarket.campusmarketserver.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public Result<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        return Result.ok(userService.register(request));
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return Result.ok(userService.login(request));
    }
}