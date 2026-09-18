package com.petproject.term_paper.repository;

import com.petproject.term_paper.entity.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<PropertyEntity, Long> {
}
