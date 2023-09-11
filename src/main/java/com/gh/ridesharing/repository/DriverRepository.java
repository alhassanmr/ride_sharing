package com.gh.ridesharing.repository;

import com.gh.ridesharing.entity.Driver;
import com.gh.ridesharing.entity.User;
import com.gh.ridesharing.enums.RoleType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends BaseEntityRepository<Driver> {
    List<Driver> findByIsActiveAndRoleType(Boolean isActive, RoleType roleType);
}


