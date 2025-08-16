package com.jamiur.disbursement.service;
import com.jamiur.disbursement.dto.ApplicationDTO;
import com.jamiur.disbursement.model.Application;
import com.jamiur.disbursement.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public List<Application> getApplicationsForCreditOfficer(Long userId) {
        return applicationRepository.findByAssignedCoIdAndStatusIn(userId, List.of(Application.ApplicationStatus.PENDING, Application.ApplicationStatus.UNDER_REVIEW));
    }

    public List<Application> getApplicationsForRiskOfficer(Long userId) {
        return applicationRepository.findByAssignedRoIdAndStatusIn(userId, List.of(Application.ApplicationStatus.PENDING, Application.ApplicationStatus.UNDER_REVIEW));
    }

    public List<ApplicationDTO> getApplicationsForManager(Long userId) {
        return applicationRepository.findByAssignedManagerIdAndStatusIn(userId, List.of(Application.ApplicationStatus.PENDING, Application.ApplicationStatus.UNDER_REVIEW));
    }
}
