package com.petproject.term_paper.repository;

import com.petproject.term_paper.entity.DealEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DealRepository extends JpaRepository<DealEntity, Long> {
    long countByDateOpen(LocalDate dateOpen);
}
