package com.gh.ridesharing.service;

import com.gh.ridesharing.entity.Role;
import com.gh.ridesharing.enums.RoleType;
import com.gh.ridesharing.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    private Role role;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        role = new Role();
        role.setId(1L);
        role.setRoleType(RoleType.DRIVER);
    }

    @Test
    public void testFindRoleByRoleType() {
        when(roleRepository.findByRoleType(RoleType.DRIVER)).thenReturn(Optional.of(role));

        Optional<Role> foundRole = roleService.findByRoleType(RoleType.DRIVER);

        assertTrue(foundRole.isPresent());
        assertEquals(role, foundRole.get());

        verify(roleRepository, times(1)).findByRoleType(RoleType.DRIVER);
    }

    @Test
    public void testUpdateRole() {
        Role updatedRole = new Role();
        updatedRole.setId(1L);
        updatedRole.setRoleType(RoleType.CUSTOMER);

        when(roleRepository.save(any())).thenReturn(updatedRole);

        Role result = roleService.update(role.getId(), updatedRole);

        assertNotNull(result);
        assertEquals(updatedRole.getRoleType(), result.getRoleType());

        verify(roleRepository, times(1)).save(any());
    }
}
