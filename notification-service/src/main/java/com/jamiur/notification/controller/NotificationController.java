
package com.jamiur.notification.controller;

import com.jamiur.notification.event.ApplicationSubmittedEvent;
import com.jamiur.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/notify-customer")
    public String notifyCustomer() {
        return "Customer notification endpoint";
    }

    @GetMapping("/notify-co")
    public List<ApplicationSubmittedEvent> notifyCo() {
        return notificationService.getStoredEvents();
    }

    @GetMapping("/notify-ro")
    public List<ApplicationSubmittedEvent> notifyRo() {
        return notificationService.getStoredEvents();
    }
}
