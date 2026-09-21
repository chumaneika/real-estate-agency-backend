package com.petproject.term_paper.service;

import com.petproject.term_paper.dto.AdminUserCreateRequest;
import com.petproject.term_paper.dto.AdminUserUpdateRequest;
import com.petproject.term_paper.dto.UserDTO;
import com.petproject.term_paper.dto.mapping.UserMapping;
import com.petproject.term_paper.entity.UserEntity;
import com.petproject.term_paper.entity.enums.UserRole;
import com.petproject.term_paper.repository.UserRepository;
import com.petproject.term_paper.repository.PropertyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class UserManagementService {
    private final UserRepository users;
    private final UserMapping mapping;
    private final PasswordEncoder passwordEncoder;
    private final PropertyRepository properties;

    public List<UserDTO> getUsers(UserRole role) {
        var result = role == null ? users.findAll() : users.findAllByRoleOrderByUsername(role);
        return result.stream().map(mapping::toDTO).toList();
    }

    public UserDTO create(AdminUserCreateRequest request) {
        if (request == null) throw new IllegalArgumentException("User data is required.");
        String username = required(request.getUsername(), "Username is required.");
        String email = email(request.getEmail());
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must contain at least 6 characters.");
        }
        if (users.findByUsername(username).isPresent() || users.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Username or email is already in use.");
        }
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() == null ? UserRole.CLIENT : request.getRole());
        user.setEnabled(request.getEnabled() == null || request.getEnabled());
        return mapping.toDTO(users.save(user));
    }

    @Transactional
    public UserDTO update(Long id, AdminUserUpdateRequest request) {
        if (request == null) throw new IllegalArgumentException("User data is required.");
        UserEntity user = users.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        if (request.getRole() != null && request.getRole() != UserRole.AGENT && properties.existsByAgentId(id)) {
            throw new IllegalArgumentException("Reassign this agent's properties before changing the role.");
        }
        if (request.getRole() != null) user.setRole(request.getRole());
        if (request.getEnabled() != null) user.setEnabled(request.getEnabled());
        return mapping.toDTO(user);
    }

    private String required(String value, String message) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(message);
        return value.trim();
    }

    private String email(String value) {
        String email = required(value, "Email is required.").toLowerCase();
        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("Enter a valid email address.");
        }
        return email;
    }
}
