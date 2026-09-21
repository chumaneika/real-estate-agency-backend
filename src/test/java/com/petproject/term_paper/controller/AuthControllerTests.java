package com.petproject.term_paper.controller;

import com.petproject.term_paper.dto.UserDTO;
import com.petproject.term_paper.dto.LoginRequest;
import com.petproject.term_paper.dto.RegisterRequest;
import com.petproject.term_paper.dto.mapping.UserMapping;
import com.petproject.term_paper.entity.UserEntity;
import com.petproject.term_paper.service.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.util.Optional;
import org.mockito.ArgumentCaptor;
import com.petproject.term_paper.entity.enums.UserRole;

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

    @Test
    void registrationAlwaysCreatesClientRole() {
        RegisterRequest registration = new RegisterRequest();
        registration.setUsername("new-client");
        registration.setEmail("CLIENT@EXAMPLE.COM");
        registration.setPassword("secret12");
        when(passwordEncoder.encode("secret12")).thenReturn("encoded");
        when(users.createUser(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapping.toDTO(any())).thenReturn(new UserDTO());

        var response = controller.register(registration);

        var captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(users).createUser(captor.capture());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(UserRole.CLIENT, captor.getValue().getRole());
        assertEquals("client@example.com", captor.getValue().getEmail());
    }
}
