
package com.campusmarket.campusmarketserver.service;

import com.campusmarket.campusmarketserver.dto.LoginRequest;
import com.campusmarket.campusmarketserver.dto.LoginResponse;
import com.campusmarket.campusmarketserver.dto.RegisterRequest;

public interface UserService {

    LoginResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}