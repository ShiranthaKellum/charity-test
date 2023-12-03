package com.bezkoder.spring.security.mongodb.service;

import com.bezkoder.spring.security.mongodb.models.ERole;
import com.bezkoder.spring.security.mongodb.models.Role;
import com.bezkoder.spring.security.mongodb.models.User;
import com.bezkoder.spring.security.mongodb.payload.request.SignupRequest;
import com.bezkoder.spring.security.mongodb.repository.RoleRepository;
import com.bezkoder.spring.security.mongodb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    public User updateUserRoles(String id, SignupRequest updatedUserRequest) {
        User existingUser = userRepository.findById(id)
                .orElse(null);
        if (existingUser != null) {
            if (updatedUserRequest.getUsername() != null) {
                existingUser.setUsername(updatedUserRequest.getUsername());
            }
            if (updatedUserRequest.getEmail() != null) {
                existingUser.setEmail(updatedUserRequest.getEmail());
            }
            if (updatedUserRequest.getPassword() != null) {
                existingUser.setPassword(updatedUserRequest.getPassword());
            }
            Set<String> roleNames = updatedUserRequest.getRoles();
            Set<Role> roles = new HashSet<>();
            if (roleNames.isEmpty()) {
                return null;
            } else {
                roleNames.forEach(
                        roleName -> {
                            switch (roleName) {
                                case "admin" -> {
                                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                            .orElseThrow(() -> new RuntimeException("ERROR: Role is not found1"));
                                    roles.add(adminRole);
                                }
                            }
                        }
                );
            }
            existingUser.setRoles(roles);
            userRepository.save(existingUser);
        }
        return existingUser;
    }
}
