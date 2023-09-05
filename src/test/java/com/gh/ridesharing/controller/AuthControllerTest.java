package com.gh.ridesharing.controller;

import com.gh.ridesharing.controller.AuthController;
import com.gh.ridesharing.entity.Customer;
import com.gh.ridesharing.entity.Driver;
import com.gh.ridesharing.entity.User;
import com.gh.ridesharing.enums.RoleType;
import com.gh.ridesharing.payload.request.LoginRequest;
import com.gh.ridesharing.repository.CustomerRepository;
import com.gh.ridesharing.repository.DriverRepository;
import com.gh.ridesharing.repository.UserRepository;
import com.gh.ridesharing.security.jwt.JwtUtils;
import com.gh.ridesharing.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testAuthenticateUser_Success() throws Exception {
        // Prepare request data
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("testPassword");
        String loginRequestBody = "{\"username\":\"testUser\",\"password\":\"testPassword\"}";

        // Mock behavior of authenticationManager
        Authentication mockAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(mockAuth);

        // Mock behavior of jwtUtils
        when(jwtUtils.generateJwtToken(mockAuth)).thenReturn("sample_jwt_token");
        // Mocking the collection of authorities
        Collection<GrantedAuthority> mockAuthorities = Arrays.asList(mock(GrantedAuthority.class));

        // Mocking the authentication.getPrincipal() call to return a mock UserDetailsImpl
        UserDetailsImpl mockUserDetails = new UserDetailsImpl(1L, "testUser", "test@email.com", "testPassword",
                RoleType.CUSTOMER, mockAuthorities);
        when(mockAuth.getPrincipal()).thenReturn(mockUserDetails);

        // Perform the request
        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("sample_jwt_token"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.email").value("test@email.com"))
                .andExpect(jsonPath("$.roleType").value("CUSTOMER"));

        // Verify interactions
        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtUtils, times(1)).generateJwtToken(any());
    }

    @Test
    public void testRegisterUserWhenUsernameTaken() throws Exception {
        // Sample input
        String signUpRequestBody = "{ \"username\": \"testUser\", \"email\": \"test@email.com\", \"password\": \"testPassword\" }";

        // Mocking userRepository to return true for existsByUsername
        when(userRepository.existsByUsername("testUser")).thenReturn(true);

        // Perform the request
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Error: Username is already taken!")));
    }

    @Test
    public void testRegisterUserWhenEmailTaken() throws Exception {
        // Sample input
        String signUpRequestBody = "{ \"username\": \"testUser\", \"email\": \"test@email.com\", \"password\": \"testPassword\" }";

        // Mocking userRepository to return false for existsByUsername and true for existsByEmail
        when(userRepository.existsByUsername("testUser")).thenReturn(false);
        when(userRepository.existsByEmail("test@email.com")).thenReturn(true);

        // Perform the request
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Error: Email is already in use!")));
    }

    @Test
    public void testRegisterUserNoRoleType() throws Exception {
        // Sample input without roleType
        String signUpRequestBody = "{ \"username\": \"testUser\", \"email\": \"test@email.com\", \"password\": \"testPassword\" }";

        // Mocking userRepository to return false for existsByUsername and existsByEmail
        when(userRepository.existsByUsername("testUser")).thenReturn(false);
        when(userRepository.existsByEmail("test@email.com")).thenReturn(false);

        // Perform the request
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User registered successfully!")));

        // Verify interaction with mocked dependencies
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void testRegisterUserAsSuperUser() throws Exception {
        String signUpRequestBody = "{ \"username\": \"superUser\", \"email\": \"superuser@example.com\", \"password\": \"password\", \"roleType\": \"SUPERUSER\" }";

        // Perform the request and verify expected behavior
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User registered successfully!")));

        // Verify user got saved in userRepository
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterUserAsDriver() throws Exception {
        String signUpRequestBody = "{ \"username\": \"driver\", \"email\": \"driver@example.com\", \"password\": \"password\", \"roleType\": \"DRIVER\" }";

        // Perform the request and verify expected behavior
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User registered successfully!")));

        // Verify driver got saved in driverRepository
        verify(driverRepository, times(1)).save(any(Driver.class));
    }

    @Test
    public void testRegisterUserAsCustomer() throws Exception {
        String signUpRequestBody = "{ \"username\": \"customer\", \"email\": \"customer@example.com\", \"password\": \"password\" }";  // No roleType specified, triggering the default case.

        // Perform the request and verify expected behavior
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User registered successfully!")));

        // Verify customer got saved in customerRepository
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

}
