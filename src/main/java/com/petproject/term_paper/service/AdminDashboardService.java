package com.petproject.term_paper.service;

import com.petproject.term_paper.dto.AdminDashboardDTO;
import com.petproject.term_paper.dto.ViewingRequestDTO;
import com.petproject.term_paper.entity.ViewingRequestEntity;
import com.petproject.term_paper.entity.ViewingRequestStatus;
import com.petproject.term_paper.repository.DealRepository;
import com.petproject.term_paper.repository.PropertyRepository;
import com.petproject.term_paper.repository.UserRepository;
import com.petproject.term_paper.repository.ViewingRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class AdminDashboardService {
    private final PropertyRepository properties;
    private final UserRepository users;
    private final DealRepository deals;
    private final ViewingRequestRepository viewingRequests;

    @Transactional(readOnly = true)
    public AdminDashboardDTO getDashboard() {
        LocalDate today = LocalDate.now();
        var activity = IntStream.rangeClosed(0, 6)
                .mapToObj(offset -> today.minusDays(6L - offset))
                .map(date -> new AdminDashboardDTO.ActivityPoint(
                        date,
                        viewingRequests.countByCreatedAtGreaterThanEqualAndCreatedAtLessThan(
                                date.atStartOfDay(), date.plusDays(1).atStartOfDay()),
                        deals.countByDateOpen(date)
                ))
                .toList();

        return new AdminDashboardDTO(
                properties.count(), users.count(), deals.count(),
                viewingRequests.countByStatus(ViewingRequestStatus.PENDING),
                viewingRequests.findTop5ByOrderByCreatedAtDesc().stream().map(this::toDTO).toList(),
                activity
        );
    }

    private ViewingRequestDTO toDTO(ViewingRequestEntity entity) {
        return new ViewingRequestDTO(
                entity.getId(), entity.getProperty().getId(), entity.getProperty().getTitle(),
                entity.getUser().getUsername(), entity.getViewingDate(), entity.getViewingTime(),
                entity.getComment(), entity.getStatus(), entity.getCreatedAt()
        );
    }
}
