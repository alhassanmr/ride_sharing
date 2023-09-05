package com.gh.ridesharing.controller;

import com.gh.ridesharing.entity.User;
import com.gh.ridesharing.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testCreateUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        when(userService.createUser(user)).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"username\": \"testUser\" }")) // sample json payload
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        when(userService.getAllUsers()).thenReturn(Collections.singletonList(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("testUser"));
    }

    @Test
    public void testGetUserById() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"));
    }
    @Test
    public void testGetUserByIdNotFound() throws Exception {
        // Given: userId does not have an associated user
        Long userId = 1L;
        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/users/" + userId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateUserProfileSuccess() throws Exception {
        // Given: userId and the updated profile data
        Long userId = 1L;
        User updatedProfile = new User();
        updatedProfile.setId(userId);
        updatedProfile.setUsername("updatedUser");

        User returnedUser = new User();
        returnedUser.setId(userId);
        returnedUser.setUsername("updatedUser");

        when(userService.updateUser(userId, updatedProfile)).thenReturn(returnedUser);

        // When & Then
        mockMvc.perform(put("/api/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 1, \"username\": \"updatedUser\" }")) // example json payload for update
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updatedUser"));
    }

    @Test
    public void testChangeUserStatusSuccess() throws Exception {
        Long userId = 1L;
        Boolean newStatus = true;
        User user = new User();
        user.setId(userId);
        user.setActive(newStatus);

        when(userService.changeUserStatus(userId, newStatus)).thenReturn(user);

        mockMvc.perform(put("/api/users/" + userId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"is_active\": true }"))
                .andExpect(status().isOk());
    }

    @Test
    public void testChangeUserStatusMissingPayload() throws Exception {
        Long userId = 1L;

        mockMvc.perform(put("/api/users/" + userId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testChangeUserStatusFailure() throws Exception {
        Long userId = 1L;
        Boolean newStatus = true;

        when(userService.changeUserStatus(userId, newStatus)).thenThrow(RuntimeException.class);

        mockMvc.perform(put("/api/users/" + userId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"is_active\": true }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteUserSuccess() throws Exception {
        Long userId = 1L;

        // Mock the behavior of the userService
        doNothing().when(userService).deleteUser(userId); // assuming deleteUser method is void

        mockMvc.perform(delete("/api/users/" + userId))
                .andExpect(status().isNoContent());

        // Verify if the service method was called
        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    public void testDeleteUserFailure() throws Exception {
        Long userId = 1L;

        // Mock the behavior of the userService to throw an exception
        doThrow(RuntimeException.class).when(userService).deleteUser(userId);

        mockMvc.perform(delete("/api/users/" + userId))
                .andExpect(status().isBadRequest());

        // Verify if the service method was called
        verify(userService, times(1)).deleteUser(userId);
    }
}
