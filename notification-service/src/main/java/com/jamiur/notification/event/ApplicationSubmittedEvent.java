package com.jamiur.notification.event;

public record ApplicationSubmittedEvent(
    Long applicationId,
    Long userId,
    Long assignedCoId,
    Long assignedRoId,
    String applicationType
) {
}
