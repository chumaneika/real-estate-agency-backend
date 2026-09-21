package com.petproject.term_paper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentSummaryDTO {
    private Long id;
    private String username;
    private String email;
}
