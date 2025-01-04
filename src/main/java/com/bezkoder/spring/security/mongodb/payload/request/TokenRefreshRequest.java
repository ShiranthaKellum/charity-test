package com.bezkoder.spring.security.mongodb.payload.request;

import lombok.Getter;

/**
 * @author Shirantha Kelum
 * @date 1/2/2025
 */

@Getter
public class TokenRefreshRequest {
  private String refreshToken;
}
