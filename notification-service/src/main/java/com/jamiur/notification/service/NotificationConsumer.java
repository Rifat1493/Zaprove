package com.jamiur.notification.service;
import com.jamiur.notification.event.ApplicationSubmittedEvent;
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
    public Consumer<ApplicationSubmittedEvent> Application() {
        return event -> {
            log.info("Received ApplicationSubmittedEvent: {}", event);
            notificationService.storeEvent(event);

            // 1. Notify the customer that their application has been received.
            log.info(">>> NOTIFYING CUSTOMER [ID: {}]: Your application #{} for {} has been received and is pending review.",
                    event.customerId(), event.applicationId(), event.applicationType());

            // 2. Notify the Credit Officer (CO) group that a new application needs review.
            log.info(">>> NOTIFYING CREDIT OFFICERS: New application #{} is ready for review.", event.applicationId());

            // 3. Notify the Risk Officer (RO) group that a new application needs review.
            log.info(">>> NOTIFYING RISK OFFICERS: New application #{} is ready for review.", event.applicationId());

            // In a real application, this would trigger emails, SMS, etc.
        };
    }
}