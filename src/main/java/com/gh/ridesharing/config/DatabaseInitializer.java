package com.gh.ridesharing.config;

import com.gh.ridesharing.entity.User;
import com.gh.ridesharing.enums.RoleType;
import com.gh.ridesharing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DatabaseInitializer(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Check if the super user already exists
        Optional<User> existingSuperUser = userService.getUserByUsername("admin");

        // If the super user does not exist, then create it
        if (existingSuperUser.isEmpty()) {
            // Create a super user
            User superUser = new User();
            superUser.setUsername("admin");
            superUser.setEmail("admin@example.com");
            superUser.setPassword("adminPassword"); // Encode the password
            superUser.setRoleType(RoleType.SUPERUSER); // Assign the SUPERUSER role

            // Save the super user to the database
            userService.createUser(superUser);
        }
    }
}
