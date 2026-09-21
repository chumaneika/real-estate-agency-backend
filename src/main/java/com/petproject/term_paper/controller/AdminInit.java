package com.petproject.term_paper.controller;

import com.petproject.term_paper.entity.UserEntity;
import com.petproject.term_paper.entity.enums.UserRole;
import com.petproject.term_paper.service.UserDetailsServiceImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInit {
    private final UserDetailsServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final String adminUsername;
    private final String adminEmail;
    private final String adminPassword;

    public AdminInit(
            UserDetailsServiceImpl userService,
            PasswordEncoder passwordEncoder,
            @Value("${ADMIN_USERNAME:}") String adminUsername,
            @Value("${ADMIN_EMAIL:}") String adminEmail,
            @Value("${ADMIN_PASSWORD:}") String adminPassword
    ) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.adminUsername = adminUsername.trim();
        this.adminEmail = adminEmail.trim().toLowerCase();
        this.adminPassword = adminPassword;
    }

    @PostConstruct
    public void createAdminIfAbsent() {
        if (adminUsername.isBlank() && adminEmail.isBlank() && adminPassword.isBlank()) {
            return;
        }
        if (adminUsername.isBlank() || adminEmail.isBlank() || adminPassword.isBlank()) {
            throw new IllegalStateException("ADMIN_USERNAME, ADMIN_EMAIL and ADMIN_PASSWORD must be configured together");
        }
        if (userService.existsByUsername(adminUsername) || userService.existsByEmail(adminEmail)) {
            return;
        }

        UserEntity admin = new UserEntity();
        admin.setUsername(adminUsername);
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole(UserRole.ADMIN);
        admin.setEnabled(true);

        userService.createUser(admin);
    }
}
