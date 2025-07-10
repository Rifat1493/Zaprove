package com.jamiur.disbursementservice.service;

import com.jamiur.disbursementservice.model.dto.DecisionResponse;
import com.jamiur.disbursementservice.model.dto.MakeDecisionRequest;
import com.jamiur.disbursementservice.model.entity.Decision;
import com.jamiur.disbursementservice.model.event.CODecisionEvent;
import com.jamiur.disbursementservice.model.event.ManagerDecisionEvent;
import com.jamiur.disbursementservice.model.event.RODecisionEvent;
import com.jamiur.disbursementservice.repository.DecisionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DisbursementService {

    private final DecisionRepository decisionRepository;
    private final EventService eventService;

    @Transactional
    public DecisionResponse recordDecision(MakeDecisionRequest request, Long userId, Decision.DecisionType decisionType) {
        Decision decision = new Decision();
        decision.setApplicationId(request.getApplicationId());
        decision.setDecisionMakerId(userId);
        decision.setDecisionType(decisionType);
        decision.setVerdict(request.getVerdict());
        decision.setComments(request.getComments());

        Decision savedDecision = decisionRepository.save(decision);

        // Fire the appropriate event based on the decision type
        publishDecisionEvent(savedDecision);

        return DecisionResponse.fromEntity(savedDecision);
    }

    public List<DecisionResponse> getDecisionsForApplication(Long applicationId) {
        return decisionRepository.findByApplicationId(applicationId).stream()
                .map(DecisionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    private void publishDecisionEvent(Decision decision) {
        switch (decision.getDecisionType()) {
            case CO_DECISION -> {
                CODecisionEvent event = new CODecisionEvent(
                        decision.getDecisionId(),
                        decision.getApplicationId(),
                        decision.getDecisionMakerId(),
                        decision.getVerdict(),
                        LocalDateTime.now()
                );
                eventService.sendEvent("coDecision-out-0", event);
            }
            case RO_DECISION -> {
                RODecisionEvent event = new RODecisionEvent(
                        decision.getDecisionId(),
                        decision.getApplicationId(),
                        decision.getDecisionMakerId(),
                        decision.getVerdict(),
                        LocalDateTime.now()
                );
                eventService.sendEvent("roDecision-out-0", event);
            }
            case MANAGER_DECISION -> {
                ManagerDecisionEvent event = new ManagerDecisionEvent(
                        decision.getDecisionId(),
                        decision.getApplicationId(),
                        decision.getDecisionMakerId(),
                        decision.getVerdict(),
                        LocalDateTime.now()
                );
                eventService.sendEvent("managerDecision-out-0", event);
            }
        }
    }
}
