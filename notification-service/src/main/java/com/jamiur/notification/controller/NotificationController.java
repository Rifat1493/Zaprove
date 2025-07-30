package com.jamiur.notification.controller;
import com.jamiur.notification.model.Notification;
import com.jamiur.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;

    @GetMapping("/all")
    public ResponseEntity<List<Notification>> getAllNotifications() {
        return ResponseEntity.ok(notificationRepository.findAll());
    }

    @GetMapping("/by-user/{userId}/{userRole}")
    public ResponseEntity<List<Notification>> getNotificationsByUser(@PathVariable Long userId, @PathVariable String userRole) {
        return ResponseEntity.ok(notificationRepository.findByUserIdAndUserRole(userId, userRole));
    }
}