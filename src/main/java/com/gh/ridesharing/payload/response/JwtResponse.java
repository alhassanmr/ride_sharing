package com.gh.ridesharing.payload.response;

import com.gh.ridesharing.enums.RoleType;
import lombok.Data;

@Data
public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private Long id;
  private String username;
  private String email;
  private RoleType roleType;

  public JwtResponse(String accessToken, Long id, String username, String email, RoleType roleType) {
    this.token = accessToken;
    this.id = id;
    this.username = username;
    this.email = email;
    this.roleType = roleType;
  }
}
