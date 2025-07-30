package com.jamiur.disbursement.repository;

import com.jamiur.disbursement.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByAssignedCoId(Long userId);
    List<Application> findByAssignedRoId(Long userId);
    List<Application> findByAssignedManagerId(Long userId);

    List<Application> findByAssignedCoIdAndStatusIn(Long userId, Collection<Application.ApplicationStatus> statuses);
    List<Application> findByAssignedRoIdAndStatusIn(Long userId, Collection<Application.ApplicationStatus> statuses);
    List<Application> findByAssignedManagerIdAndStatusIn(Long userId, Collection<Application.ApplicationStatus> statuses);

    Application findByApplicationId(Long applicationId);
}
