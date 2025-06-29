package com.jamiur.core;
import com.jamiur.core.service.UserService;

import com.jamiur.core.model.dto.CreateUserRequest;
import com.jamiur.core.model.dto.UserResponse;
import com.jamiur.core.model.entity.User;
import com.jamiur.core.exception.UserAlreadyExistsException;
import com.jamiur.core.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private UserService userService;
    
    private CreateUserRequest createUserRequest;
    private User user;
    
    @BeforeEach
    void setUp() {
        createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("testuser");
        createUserRequest.setEmail("test@example.com");
        createUserRequest.setPassword("password123");
        createUserRequest.setRole(User.Role.CUSTOMER);
        createUserRequest.setIsActive(true);
        
        user = new User();
        user.setUserId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPasswordHash("hashedPassword");
        user.setRole(User.Role.CUSTOMER);
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
    }
    
    @Test
    void createUser_Success() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        
        // When
        UserResponse result = userService.createUser(createUserRequest);
        
        // Then
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals(User.Role.CUSTOMER, result.getRole());
        assertTrue(result.getIsActive());
        
        verify(userRepository).existsByUsername("testuser");
        verify(userRepository).existsByEmail("test@example.com");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }
    
    @Test
    void createUser_UsernameAlreadyExists_ThrowsException() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        
        // When & Then
        UserAlreadyExistsException exception = assertThrows(
            UserAlreadyExistsException.class,
            () -> userService.createUser(createUserRequest)
        );
        
        assertEquals("Username already exists: testuser", exception.getMessage());
        verify(userRepository).existsByUsername("testuser");
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    void createUser_EmailAlreadyExists_ThrowsException() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        
        // When & Then
        UserAlreadyExistsException exception = assertThrows(
            UserAlreadyExistsException.class,
            () -> userService.createUser(createUserRequest)
        );
        
        assertEquals("Email already exists: test@example.com", exception.getMessage());
        verify(userRepository).existsByUsername("testuser");
        verify(userRepository).existsByEmail("test@example.com");
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    void getUserById_Success() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        
        // When
        UserResponse result = userService.getUserById(1L);
        
        // Then
        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        
        verify(userRepository).findById(1L);
    }
    
    @Test
    void getUserById_UserNotFound_ThrowsException() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        
        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> userService.getUserById(1L)
        );
        
        assertEquals("User not found with ID: 1", exception.getMessage());
        verify(userRepository).findById(1L);
    }
    
    @Test
    void getUserByUsername_Success() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        
        // When
        UserResponse result = userService.getUserByUsername("testuser");
        
        // Then
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        
        verify(userRepository).findByUsername("testuser");
    }
    
    @Test
    void getUserByUsername_UserNotFound_ThrowsException() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        
        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> userService.getUserByUsername("testuser")
        );
        
        assertEquals("User not found with username: testuser", exception.getMessage());
        verify(userRepository).findByUsername("testuser");
    }
}