package com.jamiur.notification.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DecisionEvent {
    private String decisionType;
    private Long applicationId;
    private String decision;
    private Long decisionMakerId; // This will be the manager's ID when type is CO/RO
    private Long userId;
}

