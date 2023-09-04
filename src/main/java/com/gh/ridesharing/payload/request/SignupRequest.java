package com.gh.ridesharing.payload.request;

import com.gh.ridesharing.enums.RoleType;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SignupRequest {
  @NotBlank
  @Size(min = 3, max = 20)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  private RoleType roleType;

  @NotBlank
  @Size(min = 3, max = 40)
  private String password;
}
