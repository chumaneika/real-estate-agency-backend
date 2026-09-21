package com.petproject.term_paper.dto;

import com.petproject.term_paper.entity.enums.OwnerType;
import lombok.Data;

@Data
public class OwnerUpdateRequest {
    private OwnerType ownerType;
    private String phone;
    private String email;
    private String firstName;
    private String lastName;
    private String middleName;
    private String companyName;
    private String taxId;
    private String registrationNumber;
}
