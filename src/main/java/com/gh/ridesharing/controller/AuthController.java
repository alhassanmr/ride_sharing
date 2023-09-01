package com.gh.ridesharing.controller;

import com.gh.ridesharing.entity.Customer;
import com.gh.ridesharing.entity.Driver;
import com.gh.ridesharing.entity.User;
import com.gh.ridesharing.enums.RoleType;
import com.gh.ridesharing.payload.request.LoginRequest;
import com.gh.ridesharing.payload.request.SignupRequest;
import com.gh.ridesharing.payload.response.JwtResponse;
import com.gh.ridesharing.payload.response.MessageResponse;
import com.gh.ridesharing.repository.CustomerRepository;
import com.gh.ridesharing.repository.DriverRepository;
import com.gh.ridesharing.repository.RoleRepository;
import com.gh.ridesharing.repository.UserRepository;
import com.gh.ridesharing.security.jwt.JwtUtils;
import com.gh.ridesharing.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
@CrossOrigin(origins = "http://localhost:9001/", allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;
  @Autowired
  UserRepository userRepository;
  @Autowired
  PasswordEncoder encoder;
  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private DriverRepository driverRepository;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    log.info("", loginRequest);
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    return ResponseEntity.ok(new JwtResponse(jwt,
                         userDetails.getId(),
                         userDetails.getUsername(),
                         userDetails.getEmail(),
                         userDetails.getRoleType()));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Validated @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Email is already in use!"));
    }

    RoleType roleType = signUpRequest.getRoleType();

    if (roleType == null) {
      // Create and save a Customer if no role type is specified
      Customer customer = new Customer();
      customer.setUsername(signUpRequest.getUsername());
      customer.setEmail(signUpRequest.getEmail());
      customer.setPassword(encoder.encode(signUpRequest.getPassword()));
      customer.setRoleType(RoleType.CUSTOMER);
      customerRepository.save(customer);
    } else {
      switch (roleType) {
        case SUPERUSER:
          User user = new User(signUpRequest.getUsername(),
                  signUpRequest.getEmail(),
                  encoder.encode(signUpRequest.getPassword()));
          user.setRoleType(RoleType.SUPERUSER);
          userRepository.save(user);
          break;
        case DRIVER:
          // Create and save a Driver
          Driver driver = new Driver();
          driver.setUsername(signUpRequest.getUsername());
          driver.setEmail(signUpRequest.getEmail());
          driver.setPassword(encoder.encode(signUpRequest.getPassword()));
          driver.setRoleType(RoleType.DRIVER);
          driverRepository.save(driver);
          break;
        default:
          // Create and save a Customer
          Customer customer = new Customer();
          customer.setUsername(signUpRequest.getUsername());
          customer.setEmail(signUpRequest.getEmail());
          customer.setPassword(encoder.encode(signUpRequest.getPassword()));
          customer.setRoleType(RoleType.CUSTOMER);
          customerRepository.save(customer);
      }
    }

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

}
