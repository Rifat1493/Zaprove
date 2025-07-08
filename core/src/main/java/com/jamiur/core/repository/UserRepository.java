package com.jamiur.core.repository;

import com.jamiur.core.model.entity.User;
import com.jamiur.core.model.entity.User.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    Optional<User> findByRole(Role role);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}