package com.petproject.term_paper.dto;

import java.time.LocalDate;
import java.util.List;

public record AdminDashboardDTO(
        long propertyCount,
        long userCount,
        long dealCount,
        long pendingRequestCount,
        double averagePropertyPrice,
        double totalDealVolume,
        double requestConfirmationRate,
        List<ViewingRequestDTO> recentRequests,
        List<ActivityPoint> activity,
        List<BreakdownPoint> requestBreakdown,
        List<BreakdownPoint> propertyBreakdown
) {
    public record ActivityPoint(LocalDate date, long requests, long deals) {
    }

    public record BreakdownPoint(String key, long count) {
    }
}
