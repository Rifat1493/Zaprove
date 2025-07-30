package com.jamiur.notification.service;
import com.jamiur.notification.event.ApplicationSubmittedEvent;
import com.jamiur.notification.event.DecisionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;

    @Bean
    public Consumer<ApplicationSubmittedEvent> application() {
        return event -> {
            log.info("Received ApplicationSubmittedEvent: {}", event);
            notificationService.sendNotifications(event);
        };
    }

    @Bean
    public Consumer<DecisionEvent> decision() {
        return event -> {
            log.info("Received DecisionEvent: {}", event);
            notificationService.handleDecision(event);
        };
    }
}