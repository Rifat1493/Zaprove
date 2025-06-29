package com.jamiur.core;

import com.jamiur.core.model.dto.LoginRequest;
import com.jamiur.core.model.dto.LoginResponse;
import com.jamiur.core.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
    }

    @Test
    void testLoginSuccess() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("password");
        LoginResponse response = authService.login(request);
        assertNotNull(response.getToken());
    }

    @Test
    void testLoginFailure() {
        LoginRequest request = new LoginRequest();
        request.setUsername("user");
        request.setPassword("wrong");
        assertThrows(RuntimeException.class, () -> authService.login(request));
    }

    @Test
    void testLogoutAndTokenValidation() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("password");
        String token = authService.login(request).getToken();

        assertTrue(authService.isTokenValid(token));
        authService.logout("Bearer " + token);
        assertFalse(authService.isTokenValid(token));
    }
}