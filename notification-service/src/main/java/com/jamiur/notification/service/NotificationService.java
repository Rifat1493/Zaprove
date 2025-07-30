
package com.jamiur.notification.service;

import com.jamiur.notification.event.ApplicationSubmittedEvent;
import com.jamiur.notification.event.DecisionEvent;
import com.jamiur.notification.model.Notification;
import com.jamiur.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void sendNotifications(ApplicationSubmittedEvent event) {
        // Notify the customer
        String customerMessage = String.format("Your application #%d for %s has been received and is pending review.",
                event.applicationId(), event.applicationType());
        Notification customerNotification = Notification.builder()
                .applicationId(event.applicationId())
                .userId(event.userId())
                .message(customerMessage)
                .timestamp(LocalDateTime.now())
                .isRead(false)
                .userRole("CUSTOMER")
                .build();
        notificationRepository.save(customerNotification);
        log.info(">>> NOTIFYING CUSTOMER [{}]: {}", event.userId(), customerMessage);

        // Notify the assigned Credit Officer
        if (event.assignedCoId() != null) {
            String coMessage = String.format("New application #%d is ready for your review.", event.applicationId());
            Notification coNotification = Notification.builder()
                    .applicationId(event.applicationId())
                    .userId(event.assignedCoId())
                    .message(coMessage)
                    .timestamp(LocalDateTime.now())
                    .isRead(false)
                    .userRole("CO")
                    .build();
            notificationRepository.save(coNotification);
            log.info(">>> NOTIFYING CREDIT OFFICER [ID: {}]: {}", event.assignedCoId(), coMessage);
        }

        // Notify the assigned Risk Officer
        if (event.assignedRoId() != null) {
            String roMessage = String.format("New application #%d is ready for your review.", event.applicationId());
            Notification roNotification = Notification.builder()
                    .applicationId(event.applicationId())
                    .userId(event.assignedRoId())
                    .message(roMessage)
                    .timestamp(LocalDateTime.now())
                    .isRead(false)
                    .userRole("RO")
                    .build();
            notificationRepository.save(roNotification);
            log.info(">>> NOTIFYING RISK OFFICER [ID: {}]: {}", event.assignedRoId(), roMessage);
        }
    }

    public void handleDecision(DecisionEvent event) {
        if (Objects.equals(event.getDecisionType(), "co_decision") || Objects.equals(event.getDecisionType(), "ro_decision")) {
            // Notify the manager
            String managerMessage = String.format("A decision has been made on application #%d by %s. Please review.",
                    event.getApplicationId(), event.getDecisionType());
            Notification managerNotification = Notification.builder()
                    .applicationId(event.getApplicationId())
                    .userId(event.getDecisionMakerId()) // The upstream service provides the manager's ID here
                    .message(managerMessage)
                    .timestamp(LocalDateTime.now())
                    .isRead(false)
                    .userRole("MANAGER")
                    .build();
            notificationRepository.save(managerNotification);
            log.info(">>> NOTIFYING MANAGER [{}]: {}", event.getDecisionMakerId(), managerMessage);
        } else if (Objects.equals(event.getDecisionType(), "manager_decision")) {
            // Notify the customer
            String customerMessage = String.format("A final decision has been reached for your application #%d. Verdict: %s.",
                    event.getApplicationId(), event.getDecision());
            Notification customerNotification = Notification.builder()
                    .applicationId(event.getApplicationId())
                    .userId(event.getUserId())
                    .message(customerMessage)
                    .timestamp(LocalDateTime.now())
                    .isRead(false)
                    .userRole("CUSTOMER")
                    .build();
            notificationRepository.save(customerNotification);
            log.info(">>> NOTIFYING CUSTOMER about final decision [{}]: {}", event.getUserId(), customerMessage);
        }
    }
}
