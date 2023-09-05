package com.gh.ridesharing.service;

import com.gh.ridesharing.entity.User;
import com.gh.ridesharing.exception.NotFoundException;
import com.gh.ridesharing.repository.CustomerRepository;
import com.gh.ridesharing.repository.DriverRepository;
import com.gh.ridesharing.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static com.gh.ridesharing.enums.RoleType.CUSTOMER;
import static com.gh.ridesharing.enums.RoleType.DRIVER;
import static com.gh.ridesharing.enums.RoleType.SUPERUSER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_asCustomer() {
        User user = new User();
        user.setRoleType(CUSTOMER);
        user.setPassword("testPassword");

        when(passwordEncoder.encode("testPassword")).thenReturn("encodedPassword");

        userService.createUser(user);

        verify(customerRepository, times(1)).save(any());
    }


    @Test
    void getUserByUsername() {
        String username = "testUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));

        Optional<User> result = userService.getUserByUsername(username);

        assertTrue(result.isPresent());
    }

    @Test
    void createUser_asDriver() {
        User driver = new User();
        driver.setRoleType(DRIVER);
        driver.setPassword("driverPassword");

        when(passwordEncoder.encode("driverPassword")).thenReturn("encodedDriverPassword");

        userService.createUser(driver);

        verify(driverRepository, times(1)).save(any());
    }

    @Test
    void createUser_asSuperuser() {
        User superuser = new User();
        superuser.setRoleType(SUPERUSER);
        superuser.setPassword("superPassword");

        when(passwordEncoder.encode("superPassword")).thenReturn("encodedSuperPassword");

        userService.createUser(superuser);

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void deleteUser_shouldCallDeleteById() {
        Long userId = 1L;
        userService.deleteUser(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(new User()));
        assertTrue(userService.getAllUsers().size() == 1);
    }

    @Test
    void getUserById_existingUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        Optional<User> user = userService.getUserById(1L);
        assertTrue(user.isPresent());
    }

    @Test
    void getUserById_nonExistingUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<User> user = userService.getUserById(1L);
        assertFalse(user.isPresent());
    }

    @Test
    void changeUserStatus_userExists() {
        User mockUser = mock(User.class);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        userService.changeUserStatus(1L, true);

        verify(mockUser, times(1)).setActive(true);
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    void changeUserStatus_userDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            userService.changeUserStatus(1L, true);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getAllActiveDrivers() {
        when(userRepository.findByIsActiveAndRoleType(true, DRIVER)).thenReturn(Collections.singletonList(new User()));
        assertTrue(userService.getAllActiveDrivers().size() == 1);
    }

    @Test
    void updateUser_existingUser() {
        User existingUser = new User();
        existingUser.setUsername("existingUser");

        User updatedUser = new User();
        updatedUser.setUsername("updatedUser");
        updatedUser.setEmail("updated@email.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User result = userService.updateUser(1L, updatedUser);

        assertEquals("updatedUser", result.getUsername());
        assertEquals("updated@email.com", result.getEmail());

        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void updateUser_nonExistingUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User updatedUser = new User();
        updatedUser.setUsername("updatedUser");

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.updateUser(1L, updatedUser);
        });

        assertEquals("User not found with ID: 1", exception.getMessage());
    }

}
