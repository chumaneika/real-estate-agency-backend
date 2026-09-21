package com.petproject.term_paper.dto;

import com.petproject.term_paper.entity.enums.StatusDeal;

import java.time.LocalDate;

public record DealDTO(
        Long id,
        String title,
        LocalDate dateOpen,
        LocalDate dateClose,
        StatusDeal status,
        Double price,
        Long propertyId,
        Long clientId,
        String clientUsername,
        Long agentId,
        String agentUsername,
        Long legacyClientId,
        Long legacyEmployeeId
) {
}
