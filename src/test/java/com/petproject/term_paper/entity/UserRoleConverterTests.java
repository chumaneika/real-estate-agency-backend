package com.petproject.term_paper.entity;

import com.petproject.term_paper.entity.enums.UserRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserRoleConverterTests {
    private final UserRoleConverter converter = new UserRoleConverter();

    @Test
    void writesCanonicalRoleValues() {
        assertEquals("CLIENT", converter.convertToDatabaseColumn(UserRole.CLIENT));
        assertEquals("AGENT", converter.convertToDatabaseColumn(UserRole.AGENT));
    }

    @Test
    void readsLegacyValuesDuringUpgrade() {
        assertEquals(UserRole.CLIENT, converter.convertToEntityAttribute("ROLE_USER"));
        assertEquals(UserRole.AGENT, converter.convertToEntityAttribute("EMPLOYEE"));
        assertEquals(UserRole.ADMIN, converter.convertToEntityAttribute("ROLE_ADMIN"));
    }
}
