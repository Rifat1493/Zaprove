
package com.jamiur.notification.service;

import com.jamiur.notification.event.ApplicationSubmittedEvent;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class NotificationService {

    private final List<ApplicationSubmittedEvent> storedEvents = Collections.synchronizedList(new ArrayList<>());

    public void storeEvent(ApplicationSubmittedEvent event) {
        storedEvents.add(event);
    }

    public List<ApplicationSubmittedEvent> getStoredEvents() {
        return new ArrayList<>(storedEvents);
    }
}
