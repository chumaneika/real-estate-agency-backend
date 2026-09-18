package com.petproject.term_paper.controller;

import com.petproject.term_paper.entity.UserEntity;
import com.petproject.term_paper.service.UserDetailsServiceImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminInit {
    private static final String ADMIN_USERNAME = "malik9";
    private static final String ADMIN_EMAIL = "malik9@primekey.local";

    private final UserDetailsServiceImpl userService;
    private final PasswordEncoder passwordEncoder;

    public AdminInit(UserDetailsServiceImpl userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void createAdminIfAbsent() {
        if (userService.existsByUsername(ADMIN_USERNAME) || userService.existsByEmail(ADMIN_EMAIL)) {
            return;
        }

        UserEntity admin = new UserEntity();
        admin.setUsername(ADMIN_USERNAME);
        admin.setEmail(ADMIN_EMAIL);
        admin.setPassword(passwordEncoder.encode("12345678"));
        admin.setRoles("ROLE_ADMIN");
        admin.setEnabled(true);

        userService.createUser(admin);
    }
}
