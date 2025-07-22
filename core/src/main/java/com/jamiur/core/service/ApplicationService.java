package com.jamiur.core.service;

import com.jamiur.core.model.dto.ApplicationResponse;
import com.jamiur.core.model.dto.CreateApplicationRequest;
import com.jamiur.core.model.entity.Application;
import com.jamiur.core.model.entity.User;
import com.jamiur.core.model.event.ApplicationSubmittedEvent;
import com.jamiur.core.repository.ApplicationRepository;
import com.jamiur.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final EventService eventService;

    @Transactional
    public ApplicationResponse createApplication(CreateApplicationRequest request, String username) {
        User customer = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Cannot find user: " + username));

        if (customer.getRole() != User.Role.CUSTOMER) {
            throw new AccessDeniedException("Only users with the CUSTOMER role can submit applications.");
        }

        Application application = new Application();
        application.setApplicationType(request.getApplicationType());
        application.setAmount(request.getAmount());
        application.setDescription(request.getDescription());
        application.setApplicationData(request.getApplicationData()); // <-- just set the string

        application.setCustomer(customer);
        log.info("Creating application for user: {}", customer.getUserId());
        application.setStatus(Application.ApplicationStatus.PENDING);

        Application savedApplication = applicationRepository.save(application);

        // Fire the event
        ApplicationSubmittedEvent event = new ApplicationSubmittedEvent(
                savedApplication.getApplicationId(),
                customer.getUserId(),
                savedApplication.getApplicationType(),
                savedApplication.getAmount(),
                LocalDateTime.now()
        );
        eventService.sendEvent("application-out-0", event);

        return ApplicationResponse.fromEntity(savedApplication);
    }
}