package com.jamiur.core.service;

import com.jamiur.core.model.dto.CreateApplicationRequest;
import com.jamiur.core.model.entity.Application;
import com.jamiur.core.model.entity.User;
import com.jamiur.core.repository.ApplicationRepository;
import com.jamiur.core.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventService eventService;

    @InjectMocks
    private ApplicationService applicationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createApplication_Success() {
        // Given
        String username = "testuser";
        CreateApplicationRequest request = new CreateApplicationRequest("LOAN", BigDecimal.valueOf(1000), "Test loan", "{}");
        User customer = new User();
        customer.setUserId(1L);
        customer.setUsername(username);
        customer.setRole(User.Role.CUSTOMER);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(customer));
        when(applicationRepository.save(any(Application.class))).thenAnswer(invocation -> {
            Application app = invocation.getArgument(0);
            app.setApplicationId(1L);
            return app;
        });

        // When
        var response = applicationService.createApplication(request, username);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getApplicationId());
        assertNotNull(response.getSubmittedAt());
    }

    @Test
    void createApplication_UserNotFound() {
        // Given
        String username = "nonexistent";
        CreateApplicationRequest request = new CreateApplicationRequest("LOAN", BigDecimal.valueOf(1000), "Test loan", "{}");

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalStateException.class, () -> {
            applicationService.createApplication(request, username);
        });
    }

    @Test
    void createApplication_AccessDenied() {
        // Given
        String username = "adminuser";
        CreateApplicationRequest request = new CreateApplicationRequest("LOAN", BigDecimal.valueOf(1000), "Test loan", "{}");
        User admin = new User();
        admin.setUserId(2L);
        admin.setUsername(username);
        admin.setRole(User.Role.ADMIN);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(admin));

        // When & Then
        assertThrows(AccessDeniedException.class, () -> {
            applicationService.createApplication(request, username);
        });
    }
}
