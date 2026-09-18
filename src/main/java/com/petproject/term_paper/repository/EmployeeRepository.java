package com.petproject.term_paper.repository;

import com.petproject.term_paper.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    boolean existsByName(String name);
}
