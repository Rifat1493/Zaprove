package com.jamiur.core.model.dto;

import com.jamiur.core.model.entity.Application;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ApplicationResponse {

    private Long applicationId;
    private Long customerId;
    private String applicationType;
    private BigDecimal amount;
    private String description;
    private String status;
    private LocalDateTime submittedAt;
    private String applicationData;

    public static ApplicationResponse fromEntity(Application application) {
        return ApplicationResponse.builder()
                .applicationId(application.getApplicationId())
                .customerId(application.getCustomer().getUserId())
                .applicationType(application.getApplicationType())
                .amount(application.getAmount())
                .description(application.getDescription())
                .status(application.getStatus().name())
                .submittedAt(application.getSubmittedAt())
                .applicationData(application.getApplicationData())
                .build();
    }
}