package com.jamiur.disbursementservice.model.event;

import com.jamiur.disbursementservice.model.entity.Decision;
import java.time.LocalDateTime;

public record CODecisionEvent(
        Long decisionId,
        Long applicationId,
        Long creditOfficerId,
        Decision.Verdict verdict,
        LocalDateTime decisionDate
) {}
