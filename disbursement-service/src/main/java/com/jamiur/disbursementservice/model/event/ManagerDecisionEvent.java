package com.jamiur.disbursementservice.model.event;

import com.jamiur.disbursementservice.model.entity.Decision;
import java.time.LocalDateTime;

public record ManagerDecisionEvent(
        Long decisionId,
        Long applicationId,
        Long managerId,
        Decision.Verdict verdict,
        LocalDateTime decisionDate
) {}
