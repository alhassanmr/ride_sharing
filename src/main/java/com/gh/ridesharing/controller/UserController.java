package com.gh.ridesharing.controller;

import com.gh.ridesharing.entity.User;
import com.gh.ridesharing.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public UserController(UserService userService, UserDetailsService userDetailsService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User newUser) {
        log.info("Request to create a new user");
        User user = userService.createUser(newUser);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUserProfile(@PathVariable Long userId, @RequestBody User updatedProfile) {
        log.info("Request to update user profile with ID: {}", userId);
        User user = userService.updateUser(userId, updatedProfile);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        log.info("Request to delete user with ID: {}", userId);
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Request to get all users");
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

}

