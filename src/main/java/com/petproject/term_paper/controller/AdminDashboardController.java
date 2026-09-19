package com.petproject.term_paper.controller;

import com.petproject.term_paper.dto.AdminDashboardDTO;
import com.petproject.term_paper.service.AdminDashboardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/dashboard")
@AllArgsConstructor
public class AdminDashboardController {
    private final AdminDashboardService dashboardService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<AdminDashboardDTO> getDashboard() {
        return ResponseEntity.ok(dashboardService.getDashboard());
    }
}
