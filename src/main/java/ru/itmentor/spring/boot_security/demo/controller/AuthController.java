package ru.itmentor.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.configs.jwt.JwtUtils;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.pojo.*;
import ru.itmentor.spring.boot_security.demo.service.*;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserServiceImp userServiceImp;
    private final RegistrationService registrationService;

    @Autowired
    public AuthController(JwtUtils jwtUtils, AuthenticationManager authenticationManager,
                          UserServiceImp userServiceImp, RegistrationService registrationService) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.userServiceImp = userServiceImp;
        this.registrationService = registrationService;

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
                .map(item -> item.getAuthority()).toList();

        return  ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getLastName(),
                userDetails.getAge(),
                userDetails.getPassword(),
                userDetails.getEmail(),
                userDetails.getRoles()));
    }

    @PostMapping("/registration")
    public ResponseEntity<?> performRegistration(@RequestBody SignupRequest signupRequest) {
        if (userServiceImp.findByUsername(signupRequest.getUsername()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is exist"));
        }
        registrationService.registration(signupRequest);

        return ResponseEntity.ok(new MessageResponse("User CREATED"));
    }
}