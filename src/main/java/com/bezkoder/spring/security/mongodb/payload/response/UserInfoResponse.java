package com.bezkoder.spring.security.mongodb.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserInfoResponse {
  private String id;
  private String username;
  private String email;
  private List<String> roles;
  private String accessToken;
  private String refreshToken;

  public UserInfoResponse(String id, String username, String email, List<String> roles, String accessToken, String refreshToken) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.roles = roles;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }
}
