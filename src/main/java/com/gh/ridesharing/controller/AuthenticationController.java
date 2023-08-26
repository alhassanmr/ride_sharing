package com.gh.ridesharing.controller;

import com.gh.ridesharing.payload.request.AuthenticationRequest;
import com.gh.ridesharing.payload.response.AuthenticationResponse;
import com.gh.ridesharing.service.UserDetailsServiceImpl;
import com.gh.ridesharing.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthenticationController {
    private AuthenticationManager authenticationManager;
    private UserDetailsServiceImpl userDetailsService;
    private JwtUtil jwtTokenUtil;

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setUserDetailsService(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setJwtTokenUtil(JwtUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        log.info("Received authentication request for username: {}", authenticationRequest.getUsername());

        try {
            log.debug("Attempting to authenticate user: {}", authenticationRequest.getUsername());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
            log.info("Successfully authenticated user: {}", authenticationRequest.getUsername());
        } catch (Exception e) {
            log.error("Authentication failed for username: {}", authenticationRequest.getUsername(), e);
            throw new Exception("Incorrect username or password", e);
        }

        // Generate JWT token for the user
        log.debug("Generating JWT token for user: {}", authenticationRequest.getUsername());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        log.info("userDetails: {}", userDetails);
        final String jwt = jwtTokenUtil.generateToken(String.valueOf(userDetails));
        log.info("Generated JWT token for user: {}", authenticationRequest.getUsername());

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}

