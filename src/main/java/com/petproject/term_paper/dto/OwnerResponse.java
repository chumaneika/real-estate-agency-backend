package com.petproject.term_paper.dto;

import com.petproject.term_paper.entity.enums.OwnerType;

public record OwnerResponse(
        Long id,
        OwnerType ownerType,
        String phone,
        String email,
        String firstName,
        String lastName,
        String middleName,
        String companyName,
        String taxId,
        String registrationNumber,
        long propertyCount
) {
}
