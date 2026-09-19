package com.petproject.term_paper.controller;

import com.petproject.term_paper.dto.CreateViewingRequest;
import com.petproject.term_paper.dto.ViewingRequestDTO;
import com.petproject.term_paper.service.ViewingRequestService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/viewing-requests")
public class ViewingRequestController {
    private final ViewingRequestService viewingRequestService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateViewingRequest request, HttpServletRequest servletRequest) {
        Principal principal = servletRequest.getUserPrincipal();
        if (principal == null) {
            return error(HttpStatus.UNAUTHORIZED, "Sign in to request a viewing.");
        }
        try {
            ViewingRequestDTO response = viewingRequestService.create(principal.getName(), request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (EntityNotFoundException exception) {
            return error(HttpStatus.NOT_FOUND, "Property not found.");
        } catch (IllegalArgumentException exception) {
            return error(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }

    private ResponseEntity<Map<String, String>> error(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of("message", message));
    }
}
