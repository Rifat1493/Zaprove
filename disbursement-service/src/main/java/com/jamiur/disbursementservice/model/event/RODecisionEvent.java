package com.jamiur.disbursementservice.model.event;

import com.jamiur.disbursementservice.model.entity.Decision;
import java.time.LocalDateTime;

public record RODecisionEvent(
        Long decisionId,
        Long applicationId,
        Long riskOfficerId,
        Decision.Verdict verdict,
        LocalDateTime decisionDate
) {}
