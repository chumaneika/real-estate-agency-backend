package com.petproject.term_paper.controller;

import com.petproject.term_paper.entity.UserEntity;
import com.petproject.term_paper.entity.enums.UserRole;
import com.petproject.term_paper.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AdminInitTests {
    private final UserDetailsServiceImpl users = mock(UserDetailsServiceImpl.class);
    private final PasswordEncoder encoder = mock(PasswordEncoder.class);

    @Test
    void skipsInitializationWhenAdminEnvironmentIsNotConfigured() {
        new AdminInit(users, encoder, "", "", "").createAdminIfAbsent();

        verifyNoInteractions(users, encoder);
    }

    @Test
    void createsAdminFromEnvironmentConfiguration() {
        when(encoder.encode("secure-password")).thenReturn("encoded-password");
        var initializer = new AdminInit(
                users, encoder, "prime-admin", "ADMIN@EXAMPLE.COM", "secure-password"
        );

        initializer.createAdminIfAbsent();

        var captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(users).createUser(captor.capture());
        UserEntity admin = captor.getValue();
        assertEquals("prime-admin", admin.getUsername());
        assertEquals("admin@example.com", admin.getEmail());
        assertEquals("encoded-password", admin.getPassword());
        assertEquals(UserRole.ADMIN, admin.getRole());
        assertTrue(admin.isEnabled());
    }
}
