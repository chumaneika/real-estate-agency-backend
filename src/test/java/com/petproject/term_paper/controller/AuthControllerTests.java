package com.petproject.term_paper.controller;

import com.petproject.term_paper.dto.UserDTO;
import com.petproject.term_paper.dto.mapping.UserMapping;
import com.petproject.term_paper.entity.UserEntity;
import com.petproject.term_paper.service.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTests {
    private final UserDetailsServiceImpl users = mock(UserDetailsServiceImpl.class);
    private final UserMapping mapping = mock(UserMapping.class);
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final AuthController controller = new AuthController(users, mapping, mock(PasswordEncoder.class));

    @Test
    void profileRequiresSession() {
        assertEquals(HttpStatus.UNAUTHORIZED, controller.currentUser(request).getStatusCode());
        verifyNoInteractions(users);
    }

    @Test
    void profileReturnsCurrentUser() {
        Principal principal = () -> "malik9";
        UserEntity user = new UserEntity();
        user.setEnabled(true);
        UserDTO dto = new UserDTO();
        dto.setUsername("malik9");
        dto.setEmail("malik9@primekey.local");
        when(request.getUserPrincipal()).thenReturn(principal);
        when(users.findByUsername("malik9")).thenReturn(Optional.of(user));
        when(mapping.toDTO(user)).thenReturn(dto);

        var response = controller.currentUser(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void disabledAccountCannotLoadProfile() {
        UserEntity user = new UserEntity();
        user.setEnabled(false);
        when(request.getUserPrincipal()).thenReturn(() -> "malik9");
        when(users.findByUsername("malik9")).thenReturn(Optional.of(user));
        assertEquals(HttpStatus.UNAUTHORIZED, controller.currentUser(request).getStatusCode());
        verifyNoInteractions(mapping);
    }
}
