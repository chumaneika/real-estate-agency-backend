package com.petproject.term_paper.dto;

import com.petproject.term_paper.entity.enums.OwnerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerSummaryDTO {
    private Long id;
    private OwnerType ownerType;
    private String displayName;
    private String email;
    private String phone;
}
