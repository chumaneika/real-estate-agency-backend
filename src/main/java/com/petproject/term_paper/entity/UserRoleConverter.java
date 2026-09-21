package com.petproject.term_paper.entity;

import com.petproject.term_paper.entity.enums.UserRole;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class UserRoleConverter implements AttributeConverter<UserRole, String> {
    @Override
    public String convertToDatabaseColumn(UserRole role) {
        return role == null ? null : role.name();
    }

    @Override
    public UserRole convertToEntityAttribute(String value) {
        if (value == null || value.isBlank()) return UserRole.CLIENT;
        return switch (value.trim().toUpperCase()) {
            case "ADMIN", "ROLE_ADMIN" -> UserRole.ADMIN;
            case "AGENT", "ROLE_AGENT", "EMPLOYEE", "ROLE_EMPLOYEE" -> UserRole.AGENT;
            case "CLIENT", "ROLE_CLIENT", "USER", "ROLE_USER" -> UserRole.CLIENT;
            default -> throw new IllegalArgumentException("Unsupported user role: " + value);
        };
    }
}
