package com.petproject.term_paper.dto;

import java.time.LocalDate;
import java.util.List;

public record AdminDashboardDTO(
        long propertyCount,
        long userCount,
        long dealCount,
        long pendingRequestCount,
        List<ViewingRequestDTO> recentRequests,
        List<ActivityPoint> activity
) {
    public record ActivityPoint(LocalDate date, long requests, long deals) {
    }
}
