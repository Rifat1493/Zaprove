package com.jamiur.notification.repository;

import com.jamiur.notification.model.Notification;

import java.util.List;

public interface NotificationRepository {
    Notification save(Notification notification);
    List<Notification> findAll();
    List<Notification> findByUserIdAndUserRole(Long userId, String userRole);
}
