package com.gh.ridesharing.service;

import com.gh.ridesharing.exception.NotFoundException;
import com.gh.ridesharing.entity.Customer;
import com.gh.ridesharing.entity.Driver;
import com.gh.ridesharing.entity.User;
import com.gh.ridesharing.enums.RoleType;
import com.gh.ridesharing.repository.CustomerRepository;
import com.gh.ridesharing.repository.DriverRepository;
import com.gh.ridesharing.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.gh.ridesharing.enums.RoleType.DRIVER;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, CustomerRepository customerRepository, DriverRepository driverRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.driverRepository = driverRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {
        if (user.getRoleType() == RoleType.CUSTOMER) {
            Customer customer = new Customer();
            customer.setPassword(passwordEncoder.encode(user.getPassword()));
            // Copy fields from user to customer
            copyUserFieldsToSubtype(user, customer);
            customer.setRoleType(RoleType.CUSTOMER);
            return customerRepository.save(customer);
        } else if (user.getRoleType() == DRIVER) {
            Driver driver = new Driver();
            driver.setPassword(passwordEncoder.encode(user.getPassword()));
            // Copy fields from user to driver
            copyUserFieldsToSubtype(user, driver);
            driver.setRoleType(DRIVER);
            return driverRepository.save(driver);
        } else {
            return userRepository.save(user);
        }
    }

    private void copyUserFieldsToSubtype(User source, User target) {
        target.setUsername(source.getUsername());
        target.setEmail(source.getEmail());
        target.setPassword(source.getPassword());
        target.setPhoneNumber(source.getPhoneNumber());
        target.setProfilePictureUrl(source.getProfilePictureUrl());
        target.setAddress(source.getAddress());
        target.setCity(source.getCity());
        target.setCountry(source.getCountry());
    }



    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User updateUser(Long userId, User updatedUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        // Update user's properties from updatedUser
        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        user.setPhoneNumber(updatedUser.getPhoneNumber());
        user.setProfilePictureUrl(updatedUser.getProfilePictureUrl());
        user.setAddress(updatedUser.getAddress());
        user.setCity(updatedUser.getCity());
        user.setCountry(updatedUser.getCountry());

        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public User changeUserStatus(Long userId, Boolean newStatus) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setActive(newStatus); // Assuming setIsActive is the method to set the is_active field
            return userRepository.save(user);
        } else {
            throw new NotFoundException("User not found");
        }
    }
    public List<User> getAllActiveDrivers() {
        // This assumes that you have a field called 'isAvailable' in your User entity.
        return userRepository.findByIsActiveAndRoleType(true, DRIVER);
    }

}


