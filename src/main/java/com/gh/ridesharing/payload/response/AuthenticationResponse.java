package com.gh.ridesharing.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AuthenticationResponse {
    private String jwtToken;

    public AuthenticationResponse() {
        // Default constructor
    }

    public AuthenticationResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
