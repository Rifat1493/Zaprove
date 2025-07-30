package com.jamiur.core.controller;

import com.jamiur.core.model.dto.ApplicationResponse;
import com.jamiur.core.model.dto.CreateApplicationRequest;
import com.jamiur.core.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/core")
@RequiredArgsConstructor

public class LoanApplicationController {
     private final ApplicationService applicationService;

    @PostMapping("/loan-application")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ApplicationResponse> submitApplication(
            @Valid @RequestBody CreateApplicationRequest request,
            Principal principal) {
        
        ApplicationResponse response = applicationService.createApplication(request, principal.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/loan-application/{applicationId}")
    public ResponseEntity<ApplicationResponse> getApplicationById(@PathVariable Long applicationId) {
        ApplicationResponse response = applicationService.getApplicationById(applicationId);
        return ResponseEntity.ok(response);
    }
}
