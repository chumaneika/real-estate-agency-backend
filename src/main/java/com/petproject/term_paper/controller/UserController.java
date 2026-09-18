package com.petproject.term_paper.controller;

import com.petproject.term_paper.dto.UserDTO;
import com.petproject.term_paper.entity.UserEntity;
import com.petproject.term_paper.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private final UserDetailsServiceImpl userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return ResponseEntity.ok(userService.createUser(userEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok()
                .body(userService.getUserById(id));
    }
}
