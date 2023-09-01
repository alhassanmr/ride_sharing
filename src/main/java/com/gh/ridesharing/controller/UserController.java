package com.gh.ridesharing.controller;

import com.gh.ridesharing.entity.User;
import com.gh.ridesharing.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:9001/", allowCredentials = "true")
@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserDetailsService userDetailsService;

    // Constructor injection for services
    public UserController(UserService userService, UserDetailsService userDetailsService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }

    // Endpoint to create a new user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User newUser) {
        log.info("Request to create a new user");
        User user = userService.createUser(newUser);
        return ResponseEntity.ok(user);
    }

    // Endpoint to update user profile
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUserProfile(@PathVariable Long userId, @RequestBody User updatedProfile) {
        log.info("Request to update user profile with ID: {}", userId);
        User user = userService.updateUser(userId, updatedProfile);
        return ResponseEntity.ok(user);
    }

    // Endpoint to delete a user by ID
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        log.info("Request to delete user with ID: {}", userId);
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    // Endpoint to get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Request to get all users");
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
//    @CrossOrigin(origins = "http://localhost:9001")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        log.info("Request to get user by ID: {}", userId);
        Optional<User> user = userService.getUserById(userId);
        if(user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{userId}/status")
    public ResponseEntity<User> changeUserStatus(@PathVariable Long userId, @RequestBody Map<String, Boolean> payload) {
        log.info("Request to change status of user with ID: {}", userId);
        Boolean newStatus = payload.get("is_active");
        if (newStatus == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            User updatedUser = userService.changeUserStatus(userId, newStatus);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            log.error("Error changing user status: ", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
