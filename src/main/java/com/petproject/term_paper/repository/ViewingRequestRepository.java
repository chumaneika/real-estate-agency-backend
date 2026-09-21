package com.petproject.term_paper.repository;

import com.petproject.term_paper.entity.ViewingRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import com.petproject.term_paper.entity.enums.ViewingRequestStatus;

public interface ViewingRequestRepository extends JpaRepository<ViewingRequestEntity, Long> {
    List<ViewingRequestEntity> findTop5ByOrderByCreatedAtDesc();
    long countByStatus(ViewingRequestStatus status);
    long countByCreatedAtGreaterThanEqualAndCreatedAtLessThan(LocalDateTime start, LocalDateTime end);
}
