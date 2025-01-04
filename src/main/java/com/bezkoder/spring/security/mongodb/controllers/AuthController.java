package com.bezkoder.spring.security.mongodb.controllers;

import com.bezkoder.spring.security.mongodb.models.RefreshToken;
import com.bezkoder.spring.security.mongodb.models.User;
import com.bezkoder.spring.security.mongodb.payload.request.LoginRequest;
import com.bezkoder.spring.security.mongodb.payload.request.SignupRequest;
import com.bezkoder.spring.security.mongodb.payload.request.TokenRefreshRequest;
import com.bezkoder.spring.security.mongodb.payload.request.UpdateUserRolesRequest;
import com.bezkoder.spring.security.mongodb.payload.response.MessageResponse;
import com.bezkoder.spring.security.mongodb.payload.response.RoleRequestedUser;
import com.bezkoder.spring.security.mongodb.payload.response.TokenRefreshResponse;
import com.bezkoder.spring.security.mongodb.payload.response.UserInfoResponse;
import com.bezkoder.spring.security.mongodb.repository.RefreshTokenRepository;
import com.bezkoder.spring.security.mongodb.repository.RoleRepository;
import com.bezkoder.spring.security.mongodb.repository.UserRepository;
import com.bezkoder.spring.security.mongodb.security.jwt.JwtUtils;
import com.bezkoder.spring.security.mongodb.security.services.UserDetailsImpl;
import com.bezkoder.spring.security.mongodb.service.RefreshTokenService;
import com.bezkoder.spring.security.mongodb.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

//for Angular Client (withCredentials)
//@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  UserService userService;

  @Autowired
  RefreshTokenService refreshTokenService;

  @Autowired
  RefreshTokenRepository refreshTokenRepository;

  @PostMapping("/sign-in")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    String jwtCookie = jwtUtils.generateJwtToken(userDetails);
    RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

    List<String> roles = userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());

    return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, jwtCookie)
            .body(new UserInfoResponse(userDetails.getId(),
                                   userDetails.getUsername(),
                                   userDetails.getEmail(),
                                   roles,
                                   jwtCookie,
                                   refreshToken.getToken()
                ));
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
    String requestRefreshToken = request.getRefreshToken();

    return refreshTokenRepository.findByToken(requestRefreshToken)
            .map(refreshToken -> refreshTokenService.verifyExpiration(refreshToken))
            .map(RefreshToken::getUserId)
            .map(userId -> {
              String newAccessToken = jwtUtils.generateTokenFromUsername(userId);

              return ResponseEntity.ok(
                      TokenRefreshResponse.builder()
                              .accessToken(newAccessToken)
                              .refreshToken(requestRefreshToken)
                              .build());

            })
            .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
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
    User newUser = userService.signUpUser(signUpRequest);
    return new  ResponseEntity<>(newUser, HttpStatus.OK);
  }

  @PutMapping("/user/{userId}/update-roles")
  public ResponseEntity<?> updateRoles(@PathVariable String userId, @Valid @RequestBody UpdateUserRolesRequest updatedUserRolesRequest) {
    if (!userRepository.existsByUsername(updatedUserRolesRequest.getUsername())) {
      log.error("User {} is not found", updatedUserRolesRequest.getUsername());
      return new ResponseEntity<>("User is not found!", HttpStatus.NOT_FOUND);
    }
    User updatedUser = userService.updateUserRoles(userId, updatedUserRolesRequest);
    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
  }

  @GetMapping("/role-requested-users")
  public List<RoleRequestedUser> getRoleRequestedUsers() {
    return userService.getRoleRequestedUsers();
  }
}
