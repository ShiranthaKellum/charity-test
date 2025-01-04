package com.bezkoder.spring.security.mongodb.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * @author Shirantha Kelum
 * @date 1/2/2025
 */

@Getter
@Setter
@Document(collection = "refresh-token")
public class RefreshToken {
    @Id
    private long id;
    private String token;
    private String userId;
    private Instant expiryDate;
}
