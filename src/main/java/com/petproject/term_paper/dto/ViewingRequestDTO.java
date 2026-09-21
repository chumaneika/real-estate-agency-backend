package com.petproject.term_paper.dto;

import com.petproject.term_paper.entity.enums.ViewingRequestStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ViewingRequestDTO(
        Long id,
        Long propertyId,
        String propertyTitle,
        String username,
        LocalDate viewingDate,
        LocalTime viewingTime,
        String comment,
        ViewingRequestStatus status,
        LocalDateTime createdAt
) {
}
