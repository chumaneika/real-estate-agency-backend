package com.petproject.term_paper.config;

import com.petproject.term_paper.entity.UserEntity;
import com.petproject.term_paper.entity.enums.UserRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDetailsImplTests {
    @Test
    void convertsDomainRoleToSpringAuthorityInOnePlace() {
        UserEntity user = new UserEntity();
        user.setRole(UserRole.AGENT);

        var authorities = new UserDetailsImpl(user).getAuthorities();

        assertEquals("ROLE_AGENT", authorities.iterator().next().getAuthority());
    }
}
