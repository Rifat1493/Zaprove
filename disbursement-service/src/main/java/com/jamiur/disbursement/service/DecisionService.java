package com.jamiur.disbursement.service;

import com.jamiur.disbursement.dto.DecisionRequest;
import com.jamiur.disbursement.event.DecisionEvent;
import com.jamiur.disbursement.model.Application;
import com.jamiur.disbursement.model.Decision;
import com.jamiur.disbursement.repository.ApplicationRepository;
import com.jamiur.disbursement.repository.DecisionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DecisionService {

    private final DecisionRepository decisionRepository;
    private final ApplicationRepository applicationRepository;
    private final StreamBridge streamBridge;

    public void processCreditOfficerDecision(Long applicationId, DecisionRequest decisionRequest) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + applicationId));
        Long creditOfficerId = Objects.requireNonNull(application.getAssignedCoId(), "Assigned Credit Officer not found");
        Long userId = application.getCustomerId();
        saveDecision(applicationId, creditOfficerId, "co_decision", decisionRequest);
        streamBridge.send("decision-out-0", new DecisionEvent("co_decision", applicationId, decisionRequest.getDecision(),creditOfficerId,userId));
    }

    public void processRiskOfficerDecision(Long applicationId, DecisionRequest decisionRequest) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + applicationId));
        Long riskOfficerId = Objects.requireNonNull(application.getAssignedRoId(), "Assigned Risk Officer not found");
        Long userId = application.getCustomerId();
        saveDecision(applicationId, riskOfficerId, "ro_decision", decisionRequest);
        streamBridge.send("decision-out-0", new DecisionEvent("ro_decision", applicationId, decisionRequest.getDecision(),riskOfficerId,userId));
    }

    public void processManagerDecision(Long applicationId, DecisionRequest decisionRequest) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + applicationId));
        Long managerId = Objects.requireNonNull(application.getAssignedManagerId(), "Assigned Manager not found");
        Long userId = application.getCustomerId();
        saveDecision(applicationId, managerId, "manager_decision", decisionRequest);
        streamBridge.send("decision-out-0", new DecisionEvent("manager_decision", applicationId, decisionRequest.getDecision(),managerId,userId));
    }

    private void saveDecision(Long applicationId, Long decisionMakerId, String decisionType, DecisionRequest decisionRequest) {
        Decision decision = Decision.builder()
                .applicationId(applicationId)
                .decisionMakerId(decisionMakerId)
                .decisionType(decisionType)
                .decision(decisionRequest.getDecision())
                .comments(decisionRequest.getComments())
                .decisionDate(LocalDateTime.now())
                .build();
        decisionRepository.save(decision);
    }
}