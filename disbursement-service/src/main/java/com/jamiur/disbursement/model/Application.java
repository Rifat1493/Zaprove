package com.jamiur.disbursement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Application {

    @Id
    @Column(name = "application_id")
    private Long applicationId;

    @Column(name = "user_id", nullable = false)
    private Long customerId;

    @Column(name = "application_type", nullable = false)
    private String applicationType;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApplicationStatus status;

    @CreationTimestamp
    @Column(name = "submitted_at", nullable = false, updatable = false)
    private LocalDateTime submittedAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "assigned_co_id")
    private Long assignedCoId;

    @Column(name = "assigned_ro_id")
    private Long assignedRoId;

    @Column(name = "assigned_manager_id")
    private Long assignedManagerId;

    public enum ApplicationStatus {
        PENDING,
        UNDER_REVIEW,
        APPROVED,
        REJECTED
    }
}
