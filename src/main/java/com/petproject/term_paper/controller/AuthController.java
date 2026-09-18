package com.petproject.term_paper.controller;

import com.petproject.term_paper.dto.LoginRequest;
import com.petproject.term_paper.dto.RegisterRequest;
import com.petproject.term_paper.dto.UserDTO;
import com.petproject.term_paper.dto.mapping.UserMapping;
import com.petproject.term_paper.entity.UserEntity;
import com.petproject.term_paper.service.UserDetailsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserDetailsServiceImpl userService;
    private final UserMapping userMapping;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            UserDetailsServiceImpl userService,
            UserMapping userMapping,
            PasswordEncoder passwordEncoder
    ) {
        this.userService = userService;
        this.userMapping = userMapping;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest servletRequest) {
        if (isBlank(request.getUsername()) || isBlank(request.getPassword())) {
            return error(HttpStatus.BAD_REQUEST, "Username and password are required.");
        }

        UserEntity user = userService.findByUsername(request.getUsername().trim())
                .filter(UserEntity::isEnabled)
                .filter(foundUser -> passwordEncoder.matches(request.getPassword(), foundUser.getPassword()))
                .orElse(null);

        if (user == null) {
            return error(HttpStatus.UNAUTHORIZED, "Incorrect username or password.");
        }

        Principal currentUser = servletRequest.getUserPrincipal();
        if (currentUser != null && currentUser.getName().equals(user.getUsername())) {
            return ResponseEntity.ok(userMapping.toDTO(user));
        }

        try {
            servletRequest.login(user.getUsername(), request.getPassword());
            return ResponseEntity.ok(userMapping.toDTO(user));
        } catch (ServletException exception) {
            return error(HttpStatus.UNAUTHORIZED, "Incorrect username or password.");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        String email = request.getEmail() == null ? "" : request.getEmail().trim().toLowerCase();
        String username = isBlank(request.getUsername())
                ? email.substring(0, Math.max(email.indexOf('@'), 0))
                : request.getUsername().trim();

        if (!email.contains("@") || username.length() < 3 || isBlank(request.getPassword())) {
            return error(HttpStatus.BAD_REQUEST, "Enter a valid email and a password. Username must contain at least 3 characters.");
        }
        if (userService.existsByUsername(username)) {
            return error(HttpStatus.CONFLICT, "This username is already in use.");
        }
        if (userService.existsByEmail(email)) {
            return error(HttpStatus.CONFLICT, "This email is already in use.");
        }

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles("ROLE_USER");
        user.setEnabled(true);

        UserDTO response = userMapping.toDTO(userService.createUser(user));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> currentUser(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return error(HttpStatus.UNAUTHORIZED, "Sign in to view your profile.");
        }

        return userService.findByUsername(principal.getName())
                .filter(UserEntity::isEnabled)
                .<ResponseEntity<?>>map(user -> ResponseEntity.ok(userMapping.toDTO(user)))
                .orElseGet(() -> error(HttpStatus.UNAUTHORIZED, "Your account is unavailable."));
    }

    private ResponseEntity<Map<String, String>> error(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of("message", message));
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
