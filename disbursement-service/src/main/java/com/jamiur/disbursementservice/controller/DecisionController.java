package com.jamiur.disbursementservice.controller;

import com.jamiur.disbursementservice.model.dto.DecisionResponse;
import com.jamiur.disbursementservice.model.dto.MakeDecisionRequest;
import com.jamiur.disbursementservice.model.entity.Decision;
import com.jamiur.disbursementservice.model.entity.User;
import com.jamiur.disbursementservice.service.DisbursementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/decisions")
@RequiredArgsConstructor
public class DecisionController {

    private final DisbursementService disbursementService;

    @PostMapping("/co")
    @PreAuthorize("hasAuthority('CO')")
    public ResponseEntity<DecisionResponse> makeCoDecision(
            @Valid @RequestBody MakeDecisionRequest request,
            @AuthenticationPrincipal User user) {
        DecisionResponse response = disbursementService.recordDecision(request, user.getUserId(), Decision.DecisionType.CO_DECISION);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/ro")
    @PreAuthorize("hasAuthority('RO')")
    public ResponseEntity<DecisionResponse> makeRoDecision(
            @Valid @RequestBody MakeDecisionRequest request,
            @AuthenticationPrincipal User user) {
        DecisionResponse response = disbursementService.recordDecision(request, user.getUserId(), Decision.DecisionType.RO_DECISION);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/manager")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<DecisionResponse> makeManagerDecision(
            @Valid @RequestBody MakeDecisionRequest request,
            @AuthenticationPrincipal User user) {
        DecisionResponse response = disbursementService.recordDecision(request, user.getUserId(), Decision.DecisionType.MANAGER_DECISION);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/application/{applicationId}")
    @PreAuthorize("hasAnyAuthority('CO', 'RO', 'MANAGER')")
    public ResponseEntity<List<DecisionResponse>> getDecisionsForApplication(@PathVariable Long applicationId) {
        List<DecisionResponse> decisions = disbursementService.getDecisionsForApplication(applicationId);
        return ResponseEntity.ok(decisions);
    }
}
