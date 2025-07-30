package com.jamiur.disbursement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "decisions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Decision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long applicationId;
    private Long decisionMakerId;
    private String decisionType;
    private String decision;
    private String comments;
    private LocalDateTime decisionDate;
}
