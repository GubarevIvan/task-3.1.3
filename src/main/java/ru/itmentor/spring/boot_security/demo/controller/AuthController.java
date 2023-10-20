package ru.itmentor.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.configs.jwt.JwtUtils;
import ru.itmentor.spring.boot_security.demo.model.ERole;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.pojo.JwtResponse;
import ru.itmentor.spring.boot_security.demo.pojo.LoginRequest;
import ru.itmentor.spring.boot_security.demo.pojo.MessageResponse;
import ru.itmentor.spring.boot_security.demo.pojo.SignupRequest;
import ru.itmentor.spring.boot_security.demo.service.RoleService;
import ru.itmentor.spring.boot_security.demo.service.UserServiceImp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserServiceImp userServiceImp;
    private final RoleService roleService;

    @Autowired
    public AuthController(JwtUtils jwtUtils,
                          AuthenticationManager authenticationManager,
                          UserServiceImp userServiceImp, RoleService roleService) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.userServiceImp = userServiceImp;
        this.roleService = roleService;
    }

    @PostMapping ("/login")
    public ResponseEntity<?> loginPage(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User userDetails = (User) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return  ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getLastName(),
                userDetails.getAge(),
                userDetails.getEmail(),
                userDetails.getRoles()));
    }

    @PostMapping("/registration")
    public ResponseEntity<?> performRegistration(@RequestBody SignupRequest signupRequest) {
        if (!userServiceImp.findByUsername(signupRequest.getUsername()).isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is exist"));
        }

        User user = new User(signupRequest.getUsername(), signupRequest.getLastName(),
                signupRequest.getAge(), signupRequest.getEmail());
        Set<String> reqRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (reqRoles == null) {
            Role userRole = roleService.findByUserRole(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error, role USER is not found"));
            roles.add(userRole);
        } else {
            reqRoles.forEach(r -> {
                switch (r) {
                    case "admin" -> {
                        Role adminRole = roleService.findByUserRole(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error, Role ADMIN is not found"));
                        roles.add(adminRole);
                    }
                    case "mod" -> {
                        Role modeRole = roleService.findByUserRole(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error, Role MODERATOR is not found"));
                        roles.add(modeRole);
                    }
                    default -> {
                        Role userRole = roleService.findByUserRole(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error, role USER is not found"));
                        roles.add(userRole);
                    }
                }
            });
        }
        user.setRoles(roles);
        userServiceImp.save(user);
        return ResponseEntity.ok(new MessageResponse("User CREATED"));
    }
}