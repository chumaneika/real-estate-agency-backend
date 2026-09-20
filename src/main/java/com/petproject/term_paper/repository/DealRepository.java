package com.petproject.term_paper.repository;

import com.petproject.term_paper.entity.DealEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface DealRepository extends JpaRepository<DealEntity, Long> {
    long countByDateOpen(LocalDate dateOpen);

    @Query("select coalesce(sum(d.price), 0) from DealEntity d where d.price is not null")
    Double totalVolume();
}
