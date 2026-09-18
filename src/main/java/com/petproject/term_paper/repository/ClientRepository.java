package com.petproject.term_paper.repository;

import com.petproject.term_paper.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    boolean existsByEmail(String email);
}
