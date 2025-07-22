package com.jamiur.core.controller;

import com.jamiur.core.model.dto.ApplicationResponse;
import com.jamiur.core.model.dto.CreateApplicationRequest;
import com.jamiur.core.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/core")
@RequiredArgsConstructor

public class LoanApplicationController {
     private final ApplicationService applicationService;

    @PostMapping("/submit-application")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ApplicationResponse> submitApplication(
            @Valid @RequestBody CreateApplicationRequest request,
            Principal principal) {
        
        ApplicationResponse response = applicationService.createApplication(request, principal.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
