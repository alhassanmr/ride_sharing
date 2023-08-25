package com.gh.ridesharing.repository;

import com.gh.ridesharing.entity.Role;
import com.gh.ridesharing.enums.RoleType;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Annotation indicating that this interface is a repository component managed by Spring Data
@Repository
public interface RoleRepository extends BaseEntityRepository<Role> {

    /**
     * Find a role by its role type.
     *
     * @param roleType The role type to search for.
     * @return An optional containing the role if found, or empty optional if not.
     */
    Optional<Role> findByRoleType(RoleType roleType);
}
