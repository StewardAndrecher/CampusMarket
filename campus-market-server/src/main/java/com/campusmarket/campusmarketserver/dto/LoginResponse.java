
package com.campusmarket.campusmarketserver.dto;

import lombok.Data;

@Data
public class LoginResponse {

    private String token;    // JWT，后续接口带着它访问
    private Long userId;
    private String username;
    private String nickname;
}