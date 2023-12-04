package com.bezkoder.spring.security.mongodb.service;

import com.bezkoder.spring.security.mongodb.models.ERole;
import com.bezkoder.spring.security.mongodb.models.Role;
import com.bezkoder.spring.security.mongodb.models.User;
import com.bezkoder.spring.security.mongodb.payload.request.SignupRequest;
import com.bezkoder.spring.security.mongodb.payload.request.UpdateUserRolesRequest;
import com.bezkoder.spring.security.mongodb.repository.RoleRepository;
import com.bezkoder.spring.security.mongodb.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    public User updateUserRoles(String id, UpdateUserRolesRequest updatedUserRolesRequest) {
        User existingUser = userRepository.findById(id)
                .orElse(null);
        if (existingUser != null) {
            if (updatedUserRolesRequest.getUsername() != null) {
                existingUser.setUsername(updatedUserRolesRequest.getUsername());
            }
            Set<String> roleNames = updatedUserRolesRequest.getRoles();
            Set<Role> roles = new HashSet<>();
            if (roleNames.isEmpty()) {
                return null;
            } else {
                roleNames.forEach(
                        roleName -> {
                            switch (roleName) {
                                case "admin" -> {
                                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                            .orElseThrow(() -> new RuntimeException("ERROR: Role " + roleName + " is not found!"));
                                    roles.add(adminRole);
                                    log.info("Role {} is added to username {}", roleName, updatedUserRolesRequest.getUsername());
                                }
                                case "doctor" -> {
                                    Role doctorRole = roleRepository.findByName(ERole.ROLE_DOCTOR)
                                            .orElseThrow(() -> new RuntimeException("ERROR: Role " + roleName + " is not found!"));
                                    roles.add(doctorRole);
                                    log.info("Role {} is added to username {}", roleName, updatedUserRolesRequest.getUsername());
                                }
                                case "contributor" -> {
                                    Role contributorRole = roleRepository.findByName(ERole.ROLE_CONTRIBUTOR)
                                            .orElseThrow(() -> new RuntimeException("ERROR: Role " + roleName + " is not found!"));
                                    roles.add(contributorRole);
                                    log.info("Role {} is added to username {}", roleName, updatedUserRolesRequest.getUsername());
                                }
                            }
                        }
                );
            }
            existingUser.setRoles(roles);
            userRepository.save(existingUser);
            log.info("User is updated");
        }
        return existingUser;
    }
}
