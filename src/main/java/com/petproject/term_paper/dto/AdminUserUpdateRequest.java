package com.petproject.term_paper.dto;

import com.petproject.term_paper.entity.enums.UserRole;
import lombok.Data;

@Data
public class AdminUserUpdateRequest {
    private UserRole role;
    private Boolean enabled;
}
