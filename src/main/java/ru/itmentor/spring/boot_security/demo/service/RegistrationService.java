package ru.itmentor.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itmentor.spring.boot_security.demo.model.*;
import ru.itmentor.spring.boot_security.demo.pojo.SignupRequest;
import ru.itmentor.spring.boot_security.demo.repositories.*;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class RegistrationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UserRepository userRepository, RoleRepository roleRepository,
                                   PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registration(SignupRequest signupRequest) {

        User user = new User(signupRequest.getUsername(), signupRequest.getLastName(),
                signupRequest.getAge(), signupRequest.getEmail(), signupRequest.getPassword());
        if (signupRequest.getId() != null) user.setId(signupRequest.getId());
        Set<String> reqRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (reqRoles == null) {
            Role userRole = roleRepository.findByUserRole("ROLE_USER")
                        .orElseThrow(() -> new RuntimeException("Error, role USER is not found"));
            roles.add(userRole);
        } else {
            reqRoles.forEach(r -> {
                switch (r) {
                    case "admin" -> {
                        Role adminRole = roleRepository.findByUserRole("ROLE_ADMIN")
                                    .orElseThrow(() -> new RuntimeException("Error, Role ADMIN is not found"));
                        roles.add(adminRole);
                    }
                    case "mod" -> {
                        Role modeRole = roleRepository.findByUserRole("ROLE_MODERATOR")
                                    .orElseThrow(() -> new RuntimeException("Error, Role MODERATOR is not found"));
                        roles.add(modeRole);
                    }
                    default -> {
                        Role userRole = roleRepository.findByUserRole("ROLE_USER")
                                    .orElseThrow(() -> new RuntimeException("Error, role USER is not found"));
                        roles.add(userRole);
                    }
                }
            });
        }
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}