package com.gh.ridesharing.service;

import com.gh.ridesharing.entity.Role;
import com.gh.ridesharing.enums.RoleType;
import com.gh.ridesharing.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class RoleService extends BaseServiceImpl<Role> {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        super(roleRepository);
        this.roleRepository = roleRepository;
    }

    /**
     * Find a role by its role type.
     *
     * @param roleType The role type to search for.
     * @return An optional containing the role if found, or empty optional if not.
     */
    public Optional<Role> findByRoleType(RoleType roleType) {
        return roleRepository.findByRoleType(roleType);
    }

    /**
     * Update an existing role by ID.
     *
     * @param roleId      The ID of the role to update.
     * @param updatedRole The updated role data.
     * @return The updated role entity.
     * @throws EntityNotFoundException If the role with the given ID is not found.
     */
    public Role update(Long roleId, Role updatedRole) {
        // Get the existing role by ID or throw an exception if not found
        Role existingRole = getById(roleId).orElseThrow(() -> new EntityNotFoundException("Role not found"));

        // Update the attributes of the existing role with attributes from the updated role
        existingRole.setRoleType(updatedRole.getRoleType());

        // Save the updated role to the database
        return roleRepository.save(existingRole);
    }
}



