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
    private String customerName;
    private String applicationType;
    private BigDecimal amount;
    private String description;
    private String status;
    private LocalDateTime submittedAt;
    private String applicationData;
    private Long assignedCoId;
    private Long assignedRoId;
    private Long assignedManagerId;

    public static ApplicationResponse fromEntity(Application application) {
        return ApplicationResponse.builder()
                .applicationId(application.getApplicationId())
                .customerId(application.getCustomer().getUserId())
                .customerName(application.getCustomer().getUsername())
                .applicationType(application.getApplicationType())
                .amount(application.getAmount())
                .description(application.getDescription())
                .status(application.getStatus().name())
                .submittedAt(application.getSubmittedAt())
                .applicationData(application.getApplicationData())
                .assignedCoId(application.getAssignedCoId() != null ? application.getAssignedCoId().getUserId() : null)
                .assignedRoId(application.getAssignedRoId() != null ? application.getAssignedRoId().getUserId() : null)
                .assignedManagerId(application.getAssignedManagerId() != null ? application.getAssignedManagerId().getUserId() : null)
                .build();
    }
}