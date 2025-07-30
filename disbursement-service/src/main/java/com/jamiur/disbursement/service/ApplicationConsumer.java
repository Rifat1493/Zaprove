package com.jamiur.disbursement.service;

import com.jamiur.disbursement.event.ApplicationSubmittedEvent;
import com.jamiur.disbursement.model.Application;
import com.jamiur.disbursement.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class ApplicationConsumer {

    private final ApplicationRepository applicationRepository;

    @Bean
    public Consumer<ApplicationSubmittedEvent> application() {
        return event -> {
            Application application = applicationRepository.findByApplicationId(event.applicationId());
            if (application != null) {
                application.setAssignedCoId(event.assignedCoId());
                application.setAssignedRoId(event.assignedRoId());
                applicationRepository.save(application);
            }
        };
    }
}
