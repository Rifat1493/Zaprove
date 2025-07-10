package com.jamiur.disbursementservice.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "decisions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Decision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "decision_id")
    private Long decisionId;

    @Column(name = "application_id", nullable = false)
    private Long applicationId;

    @Column(name = "decision_maker_id", nullable = false)
    private Long decisionMakerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "decision_type", nullable = false)
    private DecisionType decisionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "verdict", nullable = false)
    private Verdict verdict;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    @CreationTimestamp
    @Column(name = "decision_date", nullable = false, updatable = false)
    private LocalDateTime decisionDate;

    public enum DecisionType {
        CO_DECISION,
        RO_DECISION,
        MANAGER_DECISION
    }

    public enum Verdict {
        APPROVED,
        REJECTED,
        PENDING_REVIEW
    }
}
