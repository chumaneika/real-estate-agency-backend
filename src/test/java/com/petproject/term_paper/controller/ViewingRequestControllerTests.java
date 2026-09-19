package com.petproject.term_paper.controller;

import com.petproject.term_paper.dto.CreateViewingRequest;
import com.petproject.term_paper.dto.ViewingRequestDTO;
import com.petproject.term_paper.entity.ViewingRequestStatus;
import com.petproject.term_paper.service.ViewingRequestService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ViewingRequestControllerTests {
    private final ViewingRequestService service = mock(ViewingRequestService.class);
    private final HttpServletRequest servletRequest = mock(HttpServletRequest.class);
    private final ViewingRequestController controller = new ViewingRequestController(service);

    @Test
    void requiresAuthenticatedSession() {
        var response = controller.create(new CreateViewingRequest(), servletRequest);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verifyNoInteractions(service);
    }

    @Test
    void createsViewingRequest() {
        CreateViewingRequest request = new CreateViewingRequest();
        when(servletRequest.getUserPrincipal()).thenReturn(() -> "malik9");
        ViewingRequestDTO dto = new ViewingRequestDTO(
                1L, 2L, "Garden home", "malik9", LocalDate.now().plusDays(2),
                LocalTime.of(10, 0), null, ViewingRequestStatus.PENDING, LocalDateTime.now()
        );
        when(service.create("malik9", request)).thenReturn(dto);
        var response = controller.create(request, servletRequest);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void returnsValidationMessage() {
        CreateViewingRequest request = new CreateViewingRequest();
        when(servletRequest.getUserPrincipal()).thenReturn(() -> "malik9");
        when(service.create("malik9", request)).thenThrow(new IllegalArgumentException("Choose a future date and time."));
        var response = controller.create(request, servletRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
