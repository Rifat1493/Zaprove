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

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final EventService eventService;
    private final Random random = new Random();

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
        application.setApplicationData(request.getApplicationData());
        application.setCustomer(customer);
        log.info("Creating application for user: {}", customer.getUserId());
        application.setStatus(Application.ApplicationStatus.PENDING);

        // Assign CO, RO, and Manager
        assignOfficers(application);

        Application savedApplication = applicationRepository.save(application);

        // Fire the event
        ApplicationSubmittedEvent event = new ApplicationSubmittedEvent(
                savedApplication.getApplicationId(),
                customer.getUserId(),
                savedApplication.getAssignedCoId() != null ? savedApplication.getAssignedCoId().getUserId() : null,
                savedApplication.getAssignedRoId() != null ? savedApplication.getAssignedRoId().getUserId() : null,
                savedApplication.getApplicationType()
        );
        eventService.sendEvent("application-out-0", event);

        return ApplicationResponse.fromEntity(savedApplication);
    }

    private void assignOfficers(Application application) {
        List<User> cos = userRepository.findAllByRole(User.Role.CO);
        List<User> ros = userRepository.findAllByRole(User.Role.RO);
        List<User> managers = userRepository.findAllByRole(User.Role.MANAGER);

        if (!cos.isEmpty()) {
            application.setAssignedCoId(cos.get(random.nextInt(cos.size())));
        }
        if (!ros.isEmpty()) {
            application.setAssignedRoId(ros.get(random.nextInt(ros.size())));
        }
        if (!managers.isEmpty()) {
            application.setAssignedManagerId(managers.get(random.nextInt(managers.size())));
        }
    }

    public ApplicationResponse getApplicationById(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        return ApplicationResponse.fromEntity(application);
    }

    @Transactional
    public void updateApplicationStatus(Long applicationId, String status) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        application.setStatus(Application.ApplicationStatus.valueOf(status));
        applicationRepository.save(application);
    }
}
