package com.jamiur.disbursement.controller;
import com.jamiur.disbursement.dto.ApplicationDTO;
import com.jamiur.disbursement.dto.DecisionRequest;
import com.jamiur.disbursement.model.Application;
import com.jamiur.disbursement.service.ApplicationService;
import com.jamiur.disbursement.service.DecisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DisbursementController {

    private final DecisionService decisionService;
    private final ApplicationService applicationService;

    
    @GetMapping("/test-dis")
    // @PreAuthorize("hasAuthority('ROLE_CO')")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("Hello from disbursement service!");
    }



    @PostMapping("/decisions/credit-officer/{applicationId}")
    public ResponseEntity<Void> creditOfficerDecision(@PathVariable Long applicationId, @RequestBody DecisionRequest decisionRequest) {
        decisionService.processCreditOfficerDecision(applicationId, decisionRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/decisions/risk-officer/{applicationId}")
    public ResponseEntity<Void> riskOfficerDecision(@PathVariable Long applicationId, @RequestBody DecisionRequest decisionRequest) {
        decisionService.processRiskOfficerDecision(applicationId, decisionRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/decisions/manager/{applicationId}")
    public ResponseEntity<Void> managerDecision(@PathVariable Long applicationId, @RequestBody DecisionRequest decisionRequest) {
        decisionService.processManagerDecision(applicationId, decisionRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/applications/active/credit-officer/{userId}")
    public ResponseEntity<List<Application>> getApplicationsForCreditOfficer(@PathVariable Long userId) {
        return ResponseEntity.ok(applicationService.getApplicationsForCreditOfficer(userId));
    }

    @GetMapping("/applications/active/risk-officer/{userId}")
    public ResponseEntity<List<Application>> getApplicationsForRiskOfficer(@PathVariable Long userId) {
        return ResponseEntity.ok(applicationService.getApplicationsForRiskOfficer(userId));
    }

    @GetMapping("/applications/active/manager/{userId}")
    public ResponseEntity<List<ApplicationDTO>> getApplicationsForManager(@PathVariable Long userId) {
        return ResponseEntity.ok(applicationService.getApplicationsForManager(userId));
    }
}