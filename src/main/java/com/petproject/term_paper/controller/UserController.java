package com.petproject.term_paper.controller;

import com.petproject.term_paper.dto.AdminUserCreateRequest;
import com.petproject.term_paper.dto.AdminUserUpdateRequest;
import com.petproject.term_paper.dto.UserDTO;
import com.petproject.term_paper.entity.enums.UserRole;
import com.petproject.term_paper.service.UserDetailsServiceImpl;
import com.petproject.term_paper.service.UserManagementService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private final UserDetailsServiceImpl userDetails;
    private final UserManagementService users;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getUsers(@RequestParam(required = false) UserRole role) { return users.getUsers(role); }

    @PostMapping({"", "/create"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> createUser(@RequestBody AdminUserCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(users.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody AdminUserUpdateRequest request) {
        return users.update(id, request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO getUserById(@PathVariable Long id) { return userDetails.getUserById(id); }
}
