package com.gh.ridesharing.repository;

import com.gh.ridesharing.entity.User;
import com.gh.ridesharing.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
    List<User> findByIsActiveAndRoleType(Boolean isActive, RoleType roleType);

}

