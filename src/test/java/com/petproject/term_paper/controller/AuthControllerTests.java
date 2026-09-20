package com.petproject.term_paper.controller;

import com.petproject.term_paper.dto.UserDTO;
import com.petproject.term_paper.dto.LoginRequest;
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
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final AuthController controller = new AuthController(users, mapping, passwordEncoder);

    @Test
    void loginAcceptsEmailAndAuthenticatesWithUsername() throws Exception {
        LoginRequest login = new LoginRequest();
        login.setUsername("client@primekey.local");
        login.setPassword("Client123!");
        UserEntity user = new UserEntity();
        user.setUsername("primekey_client");
        user.setEmail("client@primekey.local");
        user.setPassword("encoded-password");
        user.setEnabled(true);
        UserDTO dto = new UserDTO();
        dto.setUsername("primekey_client");

        when(users.findByUsernameOrEmail("client@primekey.local")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Client123!", "encoded-password")).thenReturn(true);
        when(mapping.toDTO(user)).thenReturn(dto);

        var response = controller.login(login, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(request).login("primekey_client", "Client123!");
    }

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
