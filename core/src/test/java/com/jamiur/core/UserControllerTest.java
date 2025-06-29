package com.jamiur.core;
import com.jamiur.core.controller.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamiur.core.model.dto.CreateUserRequest;
import com.jamiur.core.model.dto.UserResponse;
import com.jamiur.core.model.entity.User;
import com.jamiur.core.exception.UserAlreadyExistsException;
import com.jamiur.core.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)

class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private CreateUserRequest createUserRequest;
    private UserResponse userResponse;
    
    @BeforeEach
    void setUp() {
        createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("testuser");
        createUserRequest.setEmail("test@example.com");
        createUserRequest.setPassword("password123");
        createUserRequest.setRole(User.Role.CUSTOMER);
        createUserRequest.setIsActive(true);
        
        userResponse = new UserResponse();
        userResponse.setUserId(1L);
        userResponse.setUsername("testuser");
        userResponse.setEmail("test@example.com");
        userResponse.setRole(User.Role.CUSTOMER);
        userResponse.setIsActive(true);
        userResponse.setCreatedAt(LocalDateTime.now());
        userResponse.setUpdatedAt(LocalDateTime.now());
    }
    
    @Test
    void createUser_Success() throws Exception {
        // Given
        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(userResponse);
        
        // When & Then
        mockMvc.perform(post("/api/users/create-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User created successfully"))
                .andExpect(jsonPath("$.data.username").value("testuser"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"))
                .andExpect(jsonPath("$.data.role").value("CUSTOMER"));
    }
    
    @Test
    void createUser_InvalidRequest_ValidationError() throws Exception {
        // Given
        createUserRequest.setUsername(""); // Invalid username
        createUserRequest.setEmail("invalid-email"); // Invalid email
        
        // When & Then
        mockMvc.perform(post("/api/users/create-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequest)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void createUser_UserAlreadyExists() throws Exception {
        // Given
        when(userService.createUser(any(CreateUserRequest.class)))
            .thenThrow(new UserAlreadyExistsException("Username already exists: testuser"));
        
        // When & Then
        mockMvc.perform(post("/api/users/create-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Username already exists: testuser"))
                .andExpect(jsonPath("$.error").value("USER_ALREADY_EXISTS"));
    }
    
    @Test
    void createUser_InternalError() throws Exception {
        // Given
        when(userService.createUser(any(CreateUserRequest.class)))
            .thenThrow(new RuntimeException("Database error"));
        
        // When & Then
        mockMvc.perform(post("/api/users/create-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Internal server error"))
                .andExpect(jsonPath("$.error").value("INTERNAL_ERROR"));
    }
    
    @Test
    void getUserById_Success() throws Exception {
        // Given
        when(userService.getUserById(anyLong())).thenReturn(userResponse);
        
        // When & Then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.username").value("testuser"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"));
    }
    
    @Test
    void getUserById_UserNotFound() throws Exception {
        // Given
        when(userService.getUserById(anyLong()))
            .thenThrow(new RuntimeException("User not found with ID: 1"));
        
        // When & Then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("User not found with ID: 1"))
                .andExpect(jsonPath("$.error").value("USER_NOT_FOUND"));
    }
    
    @Test
    void getUserByUsername_Success() throws Exception {
        // Given
        when(userService.getUserByUsername(anyString())).thenReturn(userResponse);
        
        // When & Then
        mockMvc.perform(get("/api/users/username/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value("testuser"));
    }
    
    @Test
    void getUserByUsername_UserNotFound() throws Exception {
        // Given
        when(userService.getUserByUsername(anyString()))
            .thenThrow(new RuntimeException("User not found with username: testuser"));
        
        // When & Then
        mockMvc.perform(get("/api/users/username/testuser"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("User not found with username: testuser"))
                .andExpect(jsonPath("$.error").value("USER_NOT_FOUND"));
    }
}