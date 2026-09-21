package com.petproject.term_paper.dto;

import com.petproject.term_paper.entity.enums.UserRole;
import lombok.Data;

@Data
public class AdminUserCreateRequest {
    private String username;
    private String email;
    private String password;
    private UserRole role;
    private Boolean enabled;
}
