package com.gh.ridesharing.entity;

import com.gh.ridesharing.enums.RoleType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Entity
@Data
public class Role extends BaseEntity {

    // Enumerated role type (e.g., DRIVER, CUSTOMER, SUPERUSER)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;
}

