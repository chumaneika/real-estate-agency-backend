package com.petproject.term_paper.dto;

import com.petproject.term_paper.entity.enums.StatusDeal;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DealRequest {
    private String title;
    private LocalDate dateOpen;
    private LocalDate dateClose;
    private StatusDeal status;
    private Double price;
    private Long propertyId;
    private Long clientId;
    private Long agentId;
}
