package com.petproject.term_paper.repository;

import com.petproject.term_paper.entity.ViewingRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewingRequestRepository extends JpaRepository<ViewingRequestEntity, Long> {
}
