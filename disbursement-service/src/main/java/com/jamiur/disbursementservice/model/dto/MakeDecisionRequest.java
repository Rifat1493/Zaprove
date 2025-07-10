package com.jamiur.disbursementservice.model.dto;

import com.jamiur.disbursementservice.model.entity.Decision;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MakeDecisionRequest {

    @NotNull(message = "Application ID cannot be null")
    private Long applicationId;

    @NotNull(message = "Verdict cannot be null")
    private Decision.Verdict verdict;

    private String comments;
}
