package com.petproject.term_paper.service;

import com.petproject.term_paper.dto.AdminDashboardDTO;
import com.petproject.term_paper.dto.ViewingRequestDTO;
import com.petproject.term_paper.entity.ViewingRequestEntity;
import com.petproject.term_paper.entity.ViewingRequestStatus;
import com.petproject.term_paper.entity.PropertyType;
import com.petproject.term_paper.repository.DealRepository;
import com.petproject.term_paper.repository.PropertyRepository;
import com.petproject.term_paper.repository.UserRepository;
import com.petproject.term_paper.repository.ViewingRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
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
        long totalRequests = viewingRequests.count();
        long confirmedRequests = viewingRequests.countByStatus(ViewingRequestStatus.CONFIRMED);
        double confirmationRate = totalRequests == 0 ? 0 : confirmedRequests * 100.0 / totalRequests;
        var activity = IntStream.rangeClosed(0, 6)
                .mapToObj(offset -> today.minusDays(6L - offset))
                .map(date -> new AdminDashboardDTO.ActivityPoint(
                        date,
                        viewingRequests.countByCreatedAtGreaterThanEqualAndCreatedAtLessThan(
                                date.atStartOfDay(), date.plusDays(1).atStartOfDay()),
                        deals.countByDateOpen(date)
                ))
                .toList();
        var requestBreakdown = Arrays.stream(ViewingRequestStatus.values())
                .map(status -> new AdminDashboardDTO.BreakdownPoint(
                        status.name(), status == ViewingRequestStatus.CONFIRMED
                                ? confirmedRequests
                                : viewingRequests.countByStatus(status)))
                .toList();
        var propertyBreakdown = Arrays.stream(PropertyType.values())
                .map(type -> new AdminDashboardDTO.BreakdownPoint(type.name(), properties.countByType(type)))
                .toList();

        return new AdminDashboardDTO(
                properties.count(), users.count(), deals.count(),
                viewingRequests.countByStatus(ViewingRequestStatus.PENDING),
                valueOrZero(properties.averagePrice()), valueOrZero(deals.totalVolume()), confirmationRate,
                viewingRequests.findTop5ByOrderByCreatedAtDesc().stream().map(this::toDTO).toList(),
                activity, requestBreakdown, propertyBreakdown
        );
    }

    private double valueOrZero(Double value) {
        return value == null ? 0 : value;
    }

    private ViewingRequestDTO toDTO(ViewingRequestEntity entity) {
        return new ViewingRequestDTO(
                entity.getId(), entity.getProperty().getId(), entity.getProperty().getTitle(),
                entity.getUser().getUsername(), entity.getViewingDate(), entity.getViewingTime(),
                entity.getComment(), entity.getStatus(), entity.getCreatedAt()
        );
    }
}
