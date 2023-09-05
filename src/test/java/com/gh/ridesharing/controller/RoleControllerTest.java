package com.gh.ridesharing.controller;

import com.gh.ridesharing.entity.Role;
import com.gh.ridesharing.enums.RoleType;
import com.gh.ridesharing.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();
    }

    @Test
    public void testGetAllRoles() throws Exception {
        when(roleService.getAllRoleType()).thenReturn(Arrays.asList(RoleType.values()));

        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk());

        verify(roleService, times(1)).getAllRoleType();
    }

    @Test
    public void testGetRoleByRoleType() throws Exception {
        Role mockRole = new Role();
        mockRole.setId(1L);
        mockRole.setRoleType(RoleType.SUPERUSER);

        when(roleService.findByRoleType(RoleType.SUPERUSER)).thenReturn(Optional.of(mockRole));

        mockMvc.perform(get("/api/roles/roleType/SUPERUSER"))
                .andExpect(status().isOk());

        verify(roleService, times(1)).findByRoleType(RoleType.SUPERUSER);
    }

    @Test
    public void testUpdateRole() throws Exception {
        Role mockRole = new Role();
        mockRole.setId(1L);
        mockRole.setRoleType(RoleType.SUPERUSER);

        when(roleService.update(1L, mockRole)).thenReturn(mockRole);

        mockMvc.perform(put("/api/roles/1")
                        .contentType("application/json")
                        .content("{\"id\": 1,\"roleType\": \"SUPERUSER\"}"))
                .andExpect(status().isOk());

        verify(roleService, times(1)).update(1L, mockRole);
    }

    @Test
    public void testDeleteRole() throws Exception {
        doNothing().when(roleService).deleteById(1L);

        mockMvc.perform(delete("/api/roles/1"))
                .andExpect(status().isNoContent());

        verify(roleService, times(1)).deleteById(1L);
    }

}
