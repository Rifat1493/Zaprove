package com.jamiur.core.model.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ApplicationSubmittedEvent(
    Long applicationId,
    Long customerId,
    String applicationType,
    BigDecimal amount,
    LocalDateTime submittedAt
) {
}