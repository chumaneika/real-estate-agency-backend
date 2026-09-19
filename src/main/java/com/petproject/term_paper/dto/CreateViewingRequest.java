package com.petproject.term_paper.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreateViewingRequest {
    private Long propertyId;
    private LocalDate viewingDate;
    private LocalTime viewingTime;
    private String comment;
}
