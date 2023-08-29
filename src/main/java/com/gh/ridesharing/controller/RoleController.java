package com.gh.ridesharing.controller;

import com.gh.ridesharing.entity.Role;
import com.gh.ridesharing.enums.RoleType;
import com.gh.ridesharing.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
@Slf4j
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * Get all roles.
     *
     * @return ResponseEntity containing the list of all roles.
     */
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        log.info("Request to get all roles");
        List<Role> roles = roleService.getAll();
        return ResponseEntity.ok(roles);
    }

    /**
     * Get role by role type.
     *
     * @param roleType The type of role to search for.
     * @return ResponseEntity containing the role with the specified role type, if found.
     */
    @GetMapping("/roleType/{roleType}")
    public ResponseEntity<Role> getRoleByRoleType(@PathVariable RoleType roleType) {
        log.info("Request to get role by role type: {}", roleType);
        Optional<Role> role = roleService.findByRoleType(roleType);
        return role.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update a role by ID.
     *
     * @param roleId      The ID of the role to update.
     * @param updatedRole The updated role data.
     * @return ResponseEntity containing the updated role.
     */
    @PutMapping("/{roleId}")
    public ResponseEntity<Role> updateRole(@PathVariable Long roleId, @RequestBody Role updatedRole) {
        log.info("Request to update role with ID: {}", roleId);
        Role role = roleService.update(roleId, updatedRole);
        return ResponseEntity.ok(role);
    }

    /**
     * Delete a role by ID.
     *
     * @param roleId The ID of the role to delete.
     * @return ResponseEntity with no content.
     */
    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long roleId) {
        log.info("Request to delete role with ID: {}", roleId);
        roleService.deleteById(roleId);
        return ResponseEntity.noContent().build();
    }
}
