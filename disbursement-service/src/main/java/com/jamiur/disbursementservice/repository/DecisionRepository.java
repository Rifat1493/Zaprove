package com.jamiur.disbursementservice.repository;

import com.jamiur.disbursementservice.model.entity.Decision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DecisionRepository extends JpaRepository<Decision, Long> {
    List<Decision> findByApplicationId(Long applicationId);
}
