package com.jamiur.disbursement.repository;

import com.jamiur.disbursement.dto.ApplicationDTO;
import com.jamiur.disbursement.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    // List<Application> findByAssignedManagerIdAndStatusIn(Long userId, Collection<Application.ApplicationStatus> statuses);

    Application findByApplicationId(Long applicationId);

    @Query("""
    SELECT new com.jamiur.disbursement.dto.ApplicationDTO(
        a,
        coDec.decision,
        roDec.decision
    )
    FROM Application a
    LEFT JOIN Decision coDec
        ON a.applicationId = coDec.applicationId
        AND coDec.decisionType = 'co_decision'
    LEFT JOIN Decision roDec
        ON a.applicationId = roDec.applicationId
        AND roDec.decisionType = 'ro_decision'
    WHERE a.assignedManagerId = :managerId
      AND a.status IN :statuses
        """)
    List<ApplicationDTO> findByAssignedManagerIdAndStatusIn(
            @Param("managerId") Long userId,
            @Param("statuses") Collection<Application.ApplicationStatus> statuses
    );
}
