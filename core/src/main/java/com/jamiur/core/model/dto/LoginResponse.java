package com.jamiur.core.model.dto;

import com.jamiur.core.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private long expiresIn;
    private User.Role role;
    private Long userId;
}


