package com.gh.ridesharing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gh.ridesharing.entity.Role;
import com.gh.ridesharing.enums.RoleType;
import com.gh.ridesharing.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(RoleController.class)
public class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService roleService;

    private Role role;

    @BeforeEach
    public void setUp() {
        role = new Role();
        role.setId(1L);
        role.setRoleType(RoleType.DRIVER);
    }

    @Test
    public void testGetRoleByRoleType() throws Exception {
        when(roleService.findByRoleType(any(RoleType.class))).thenReturn(Optional.of(role));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/roles/roleType/DRIVER"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(role.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roleType").value(role.getRoleType().toString()))
                .andDo(print());
    }

    @Test
    public void testUpdateRole() throws Exception {
        Role updatedRole = new Role();
        updatedRole.setRoleType(RoleType.CUSTOMER);

        when(roleService.update(any(Long.class), any(Role.class))).thenReturn(updatedRole);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(updatedRole);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/roles/1")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(role.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roleType").value(updatedRole.getRoleType().toString()))
                .andDo(print());
    }

    @Test
    public void testDeleteRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/roles/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print());
    }
}
