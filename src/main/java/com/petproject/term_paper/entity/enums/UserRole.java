package com.petproject.term_paper.entity.enums;

public enum UserRole {
    CLIENT,
    AGENT,
    ADMIN;

    public String authority() {
        return "ROLE_" + name();
    }
}
