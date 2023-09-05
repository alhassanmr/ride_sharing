package com.gh.ridesharing.service;

import com.gh.ridesharing.entity.Role;
import com.gh.ridesharing.enums.RoleType;
import com.gh.ridesharing.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    void testFindByRoleType() {
        Role role = new Role();
        role.setRoleType(RoleType.SUPERUSER);

        when(roleRepository.findByRoleType(RoleType.SUPERUSER)).thenReturn(Optional.of(role));

        Optional<Role> result = roleService.findByRoleType(RoleType.SUPERUSER);

        assertTrue(result.isPresent());
        assertEquals(RoleType.SUPERUSER, result.get().getRoleType());
    }

    @Test
    void testUpdateRoleSuccessfully() {
        Role existingRole = new Role();
        existingRole.setRoleType(RoleType.CUSTOMER);

        Role updatedRole = new Role();
        updatedRole.setRoleType(RoleType.SUPERUSER);

        when(roleRepository.save(any(Role.class))).thenReturn(updatedRole);
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(existingRole));

        Role result = roleService.update(1L, updatedRole);

        assertEquals(RoleType.SUPERUSER, result.getRoleType());
    }

    @Test
    void testUpdateRoleNotExisting() {
        Role updatedRole = new Role();
        updatedRole.setRoleType(RoleType.SUPERUSER);

        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> roleService.update(1L, updatedRole));
    }

    @Test
    void testGetAllRoleType() {
        RoleType[] roleTypes = RoleType.values();
        assertEquals(roleTypes.length, roleService.getAllRoleType().size());
    }
}
