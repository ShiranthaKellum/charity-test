package com.bezkoder.spring.security.mongodb.payload.response;

import lombok.Builder;
import lombok.Getter;

/**
 * @author Shirantha Kelum
 * @date 1/2/2025
 */

@Getter
@Builder
public class TokenRefreshResponse {
    private String accessToken;
    private String refreshToken;
}
