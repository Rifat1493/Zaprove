package com.jamiur.notification.repository;

import com.jamiur.notification.model.Notification;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
@Primary
public class InMemoryNotificationRepository implements NotificationRepository {

    private final List<Notification> notifications = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong();

    @Override
    public Notification save(Notification notification) {
        if (notification.getId() == null) {
            notification.setId(idCounter.incrementAndGet());
        }
        notifications.add(notification);
        return notification;
    }

    @Override
    public List<Notification> findAll() {
        return new ArrayList<>(notifications);
    }

    @Override
    public List<Notification> findByUserIdAndUserRole(Long userId, String userRole) {
        return notifications.stream()
                .filter(notification -> notification.getUserId().equals(userId) && notification.getUserRole().equals(userRole))
                .collect(Collectors.toList());
    }


}