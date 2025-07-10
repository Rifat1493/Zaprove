package com.jamiur.disbursementservice.model.dto;

import com.jamiur.disbursementservice.model.entity.Decision;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DecisionResponse {

    private Long decisionId;
    private Long applicationId;
    private Long decisionMakerId;
    private String decisionType;
    private String verdict;
    private String comments;
    private LocalDateTime decisionDate;

    public static DecisionResponse fromEntity(Decision decision) {
        return DecisionResponse.builder()
                .decisionId(decision.getDecisionId())
                .applicationId(decision.getApplicationId())
                .decisionMakerId(decision.getDecisionMakerId())
                .decisionType(decision.getDecisionType().name())
                .verdict(decision.getVerdict().name())
                .comments(decision.getComments())
                .decisionDate(decision.getDecisionDate())
                .build();
    }
}
